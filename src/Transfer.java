import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import story.Character;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

class Transfer {
    private static final String HOST = "localhost";
    private static Socket socket;
    private static int port;
    private int currentTryNumber = 0;
    private static final int TRY_MAX_TIMES = 3;
    private static final int BUFFER_SIZE = 512;
    private static final int OFFSET = 69;
    private static final int SLEEP_TIME = 5000; // ms
    private String authHash;

    void getInfoForConnection() {
        System.out.println("Введите номер порта:");
        Scanner in = new Scanner(System.in);
        port = in.nextInt();
        System.out.println("Connecting to Server on port " + port + "...");
        connect();
    }

    private boolean connect() {
        try {
            socket = new Socket(InetAddress.getByName(HOST), port);
            return true;
        } catch (IOException e) {
            System.out.println("Не удалось подключиться.");
            return false;
        }

    }

    void setAuthHash(String authHash){
        this.authHash = authHash;
    }



    int readCommand() {
        int executeStatus = 0;
        ArrayList<String> jsonCommands = new ArrayList<>(Arrays.asList("add", "remove"));
        Scanner in = new Scanner(System.in);
        StringBuilder str = new StringBuilder();
        System.out.println("Введите команду:");
        try {
            String nextLine;
            nextLine = in.nextLine();
            str.append(nextLine);
            if (jsonCommands.contains(nextLine.split(" ", 2)[0])) {
                while (in.hasNextLine()) {
                    nextLine = in.nextLine();
                    if (nextLine.equals("")) {
                        break;
                    }
                    str.append(nextLine);
                }
            }
            str = new StringBuilder(str.toString());
            if (!str.toString().equals("")) {
                String[] words = str.toString().split(" ", 2);
                String cmd = words[0];
                String arg;
                if (words.length > 1) {
                    arg = words[1];
                    arg = arg.trim();
                    sendToServer(cmd, arg, false, authHash);
                } else if (jsonCommands.contains(cmd)) {
                    System.err.println("Этой команде нужно передать аругмент");
                } else {
                    sendToServer(cmd, null, false, authHash);
                }
            }
        } catch (NoSuchElementException e) {
            System.err.println("EOF");
            executeStatus = -1;
        }
        return executeStatus;
    }

    String sendToServer(String cmd, String arg, boolean isAuth, String authHash) {
        String response = send(cmd, arg, socket, isAuth, authHash);
        if (!response.equals("")) {
            return response;
        }
        while (currentTryNumber < TRY_MAX_TIMES) {
            currentTryNumber++;
            System.out.println("Сервер не отвечает. Попытка переподключения #"
                    + currentTryNumber);
            connect();
            response = send(cmd, arg, socket, isAuth, authHash);
            if (!response.equals("")) {
                return response;
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                System.err.println("Thread.sleep() вызвал исключение");
            }
        }
        System.err.println("Сервер недоступен");
        System.exit(-1);
        return "";
    }

    private String send(String cmd, String arg, Socket socket, boolean isAuth, String authHash) {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            // длина данных в первом байте
            buffer[0] = 1; // isCommand
            int len = cmd.getBytes().length;
            buffer[1] = (byte) (len / 128);
            buffer[2] = (byte) (len - (int) buffer[1] * 128);
            buffer[3] = 0; //no more fragments
            buffer[4] = (byte) (isAuth ? 1 : 0); // isAuth
            //далее идет 64 символа авторизации
            if(!authHash.equals("")){
                for (int i = 0; i < 64; i++){
                    buffer[i + 5] = authHash.getBytes()[i];
                }
            }
            // команда
            for (int i = 0; i < cmd.getBytes().length; i++) {
                buffer[i + OFFSET] = cmd.getBytes()[i];
            }
            OutputStream sout = socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(sout);
            out.write(buffer);
            out.flush();
            // System.out.println("Command was sent to server");
            if (arg != null) {
                try {
                    Character character = new Gson().fromJson(arg, Character.class);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(character);
                    byte[] characterInBytes = baos.toByteArray();
                    for (int i = 0; i < characterInBytes.length; i = i + BUFFER_SIZE - OFFSET) {
                        int capacity;
                        if (characterInBytes.length - i > BUFFER_SIZE - OFFSET) {
                            capacity = BUFFER_SIZE - OFFSET;
                            buffer[3] = 1; // more fragments
                        } else {
                            capacity = characterInBytes.length - i;
                            buffer[3] = 0; //no more fragments
                        }
                        buffer[0] = 0; // isCommand
                        buffer[1] = (byte) (capacity / 128);
                        buffer[2] = (byte) (capacity - 128 * buffer[1]);
                        for (int j = OFFSET; j < BUFFER_SIZE - 1; j++) {
                            if (i + j - OFFSET < characterInBytes.length) {
                                buffer[j] = characterInBytes[j + i - OFFSET];
                            }
                        }
                        out.write(buffer);
                        out.flush();
                    }
                } catch (JsonSyntaxException | IllegalStateException e) {
                    System.out.println("Неверный формат аргумента. Необходимо ввести строку в JSON.");
                    readCommand();
                }
            }
        } catch (IOException e) {
            return "Ошибка.";
        }
        String response = receive();
        System.out.println(response);
        if (response.equals("Ошибка авторизации пользователя.")){
            Authorization auth = new Authorization(this);
            auth.authorize();
            return "";
        }
        return response;
    }

    String receive() {
        byte[] bytes = new byte[1000000];
        byte[] newBytes;
        try {
            InputStream sin = socket.getInputStream();
            DataInputStream in = new DataInputStream(sin);
            int bytesRead = 0;
            do {
                bytesRead += in.read(bytes);
            } while (in.available() > 0);
            if (bytesRead < 0) {
                if (currentTryNumber > TRY_MAX_TIMES) {
                    System.err.println("Сервер недоступен");
                    System.exit(-1);
                }
                currentTryNumber++;
                System.out.println("Сервер не отвечает. Попытка переподключения #" + currentTryNumber);
                Thread.sleep(SLEEP_TIME);
                connect();

            } else {
                currentTryNumber = 0;
                newBytes = new byte[bytesRead];
                System.arraycopy(bytes, 0, newBytes, 0, bytesRead);
                return new String(newBytes);
            }
        } catch (IOException e) {
            System.err.println("Проблемы с соединением");
        } catch (InterruptedException e) {
            System.err.println("Thread.sleep() InterruptedException occurred");
        }
        return "";
    }


}
