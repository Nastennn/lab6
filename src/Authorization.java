import com.google.gson.Gson;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

class Authorization {
    private Transfer transfer;
    private Scanner in;
    String authHash;

    Authorization(Transfer transfer) {
        this.transfer = transfer;
    }

    void authorize() {
        in = new Scanner(System.in);
        System.out.println("1 - Вход, 2 - Регистрация");
        String log = in.nextLine();
        if (log.equals("1")) {
            login();
        } else if (log.equals("2")) {
            register();
            login();
        } else{
           authorize();
        }
    }

    private void register() {
        System.out.println("Введите email: ");
        String email = in.nextLine();
        sendAuth(createParamsJson(email, null), null, null);
    }

    private void login() {
        System.out.println("Введите email: ");
        String email = in.nextLine();
        System.out.println("Введите пароль: ");
        String password = in.nextLine();
        sendAuth(createParamsJson(email, getPasswordHash(password)), email, password);
    }

    private String createParamsJson(String email, String password) {
        ArrayList<String> params = new ArrayList<>();
        params.add(email);
        params.add(password);
        String json = new Gson().toJson(params);
       return json;
    }


    private void sendAuth(String json, String email, String password) {

        String response = transfer.sendToServer(json, null, true, "");
        if (response.equals("Неверный email или пароль.")) {
            System.out.println("Попробуйте еще раз.");
            login();
        } else if (response.equals("Не удалось отправить сообщение.")) {
            System.out.println("Попробуйте еще раз.");
            register();
        }else if (response.equals("Вход выполнен.")){
            this.authHash = authHash(email.split("@")[0], getPasswordHash(password));
            this.transfer.setAuthHash(authHash);
        }
    }

    String getPasswordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, md.digest(password.getBytes(StandardCharsets.UTF_8)));
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return "Ошибка при хешировании.";
        }
    }

    String authHash(String login, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String authHash = login + password;
            BigInteger number = new BigInteger(1, md.digest(authHash.getBytes(StandardCharsets.UTF_8)));
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length()<32){
                hexString.insert(0,'0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return "Ошибка при хешировании.";
        }
    }


}
