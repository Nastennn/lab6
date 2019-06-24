import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import story.Character;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

class Transfer {
    private static final int PORT = 9876;
    private static final String HOST = "localhost";
    private InputStream sin;
    private OutputStream sout;
    private static Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private int BUFFER_SIZE = 128;
    private int OFFSET = 3;

    void connect() {
        try {
            System.out.println("Connecting to Server on port " + PORT + "...");
            socket = new Socket(InetAddress.getByName(HOST), PORT);
        } catch (IOException e) {
            System.out.println();
        }
    }

    int readCommand() {
        int executeStatus = 0;
        ArrayList<String> jsonCommands = new ArrayList<>(Arrays.asList("add", "remove"));
        Scanner in = new Scanner(System.in);
        StringBuilder str = new StringBuilder();
        String line = null;
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
                    if (!arg.startsWith("[")) {
                        arg = "[" + arg;
                    }
                    if (!arg.endsWith("]")) {
                        arg = arg + "]";
                    }
                    sendToServer(cmd, arg, socket);
                } else if (jsonCommands.contains(cmd)) {
                    System.err.println("Этой команде нужно передать аругмент");
                } else {
                    sendToServer(cmd, null, socket);
                }
            }
        } catch (NoSuchElementException e) {
            System.err.println("EOF");
            executeStatus = -1; //разобраться нужен ли статус выполнения в клиенте
        }
        return executeStatus;
    }

    void sendToServer(String cmd, String arg, Socket socket) {
        try {
            sout = socket.getOutputStream();
            out = new DataOutputStream(sout);
            byte[] buffer = new byte[BUFFER_SIZE];
            // длина данных в первом байте
            buffer[0] = 1; // isCommand
            buffer[1] = (byte) cmd.getBytes().length;
            buffer[2] = 0; //no more fragments
            // команда
            for (int i = 0; i < cmd.getBytes().length; i++) {
                buffer[i + OFFSET] = cmd.getBytes()[i];
            }
            out.write(buffer);
            out.flush();
            System.out.println("Command was sent to server");

            if (arg != null) {
                // Vector<Character> characterVector = new Gson().fromJson(arg, TypeToken.getParameterized(Vector.class, Character.class).getType());
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                oos = new ObjectOutputStream(baos);
//                oos.write();
                byte[] characterInBytes = arg.getBytes(StandardCharsets.UTF_8);
                for (int i = 0; i < characterInBytes.length; i = i + BUFFER_SIZE - OFFSET) {
                    int capacity;
                    if (characterInBytes.length - i > BUFFER_SIZE - OFFSET) {
                        capacity = BUFFER_SIZE - OFFSET;
                        buffer[2] = 1; // more fragments
                    } else {
                        capacity = characterInBytes.length - i;
                        buffer[2] = 0; //no more fragments
                    }
                    buffer[0] = 0; // isCommand
                    buffer[1] = (byte) capacity;
                    for (int j = 3; j < BUFFER_SIZE - 1; j++) {
                        if (i + j - OFFSET < characterInBytes.length) {
                            buffer[j] = characterInBytes[j + i - OFFSET];
                        }
                    }
                    out.write(buffer);
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось получить поток вывода из сокета.");
        }
    }

    String receive() {
        byte[] bytes = new byte[2048];
        byte[] newBytes = new byte[0];
        try {
            sin = socket.getInputStream();
            in = new DataInputStream(sin);
            int bytesRead = 0;
            do {
                bytesRead += in.read(bytes);
            } while (in.available() > 0);
            newBytes = new byte[bytesRead];
            System.arraycopy(bytes, 0, newBytes, 0, bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(newBytes);

    }

    void getObject() {
        try {
            System.out.println(ois.readObject());
        } catch (IOException e) {
            System.out.println("Ошибка чтения.");
        } catch (ClassNotFoundException e) {
            System.out.println("Класс или объект не найден");
        }
    }

}
