package tut3;

import java.lang.*;
import java.net.*;

class Server {
    public static void main(String args[]) {
		try {
			if (args.length != 1) {
				System.out.println("Usage: java Server <port>");
				return;
			} else {
				process(Integer.parseInt(args[0]));
			}
		} catch (Exception e) {
			System.out.println("Error Server: " + e.getMessage());
		}
    }

    public static void process(final int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			Socket socket = serverSocket.accept();
			NetReader in = new NetReader(socket);
			String action = in.getLine();
			System.out.println(action);
		} catch (Exception e) {
			System.out.println("Error Server: " + e.getMessage());
		}
    }
}
