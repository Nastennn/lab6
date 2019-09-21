public class Main {
    public static void main(String[] args) {

        Transfer transfer = new Transfer();
        transfer.getInfoForConnection();

        Authorization authorization = new Authorization(transfer);

        authorization.authorize();
        // transfer.setAuthHash(authorization.authHash);

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> System.out.println("Программа завершилась."))
        );

        while (true) {
            transfer.readCommand();
        }

    }
}

