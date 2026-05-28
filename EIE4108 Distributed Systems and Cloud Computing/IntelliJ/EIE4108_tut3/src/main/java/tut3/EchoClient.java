package tut3;

import java.io.*;
import java.net.*;

public class EchoClient {

    public static void main(String args[]) {
        try {
            Socket s = new Socket("localhost", 9999);
            PrintStream os = new PrintStream(s.getOutputStream());
            InputStream is = s.getInputStream();
            BufferedReader brServer = new BufferedReader(new InputStreamReader(is));
            BufferedReader brKeyboard = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String message = brKeyboard.readLine();
                if (message.equals("exit")==true) {
                    s.close();
                    break;
                }
                os.println(message);
                String reply = brServer.readLine();
                System.out.println("Reply: " + reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } //end catch
    }
}