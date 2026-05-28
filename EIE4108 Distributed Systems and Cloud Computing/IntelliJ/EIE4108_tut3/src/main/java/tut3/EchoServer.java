package tut3;

import java.io.*;
import java.net.*;

public class EchoServer {
    public static void main(String args[]) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(9999);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection granted");
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream os = new PrintStream(socket.getOutputStream());
                while (true) {
                    String line = br.readLine();
                    if (line==null) {
                        socket.close();
                        break;
                    }
                    System.out.println("Receive: " + line);
                    os.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
