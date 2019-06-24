import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Transfer transfer = new Transfer();
        transfer.connect();
        System.out.println("You can type in");

        while (true) {

            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            transfer.readCommand();
//                byte[] b = new byte[55];
//                in.read(b);
//                ByteBuffer bb = ByteBuffer.allocate(55);
//                bb.put(b);
//                bb.flip();
//                String str = new String(bb.array());
//                System.out.println(str);
        }
    }



    private static void log(String str) {
        System.out.println(str);
    }
}

