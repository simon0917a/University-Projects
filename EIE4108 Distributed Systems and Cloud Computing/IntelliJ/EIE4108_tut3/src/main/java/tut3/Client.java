package tut3;

import java.lang.*;
import java.net.*;

class Client {
    public static void main(String args[]) {
		try {
			if (args.length != 3) {
				System.out.println("Usage: java Client <host> <port> <message>");
				return;
			} else {
				process(args[0], Integer.parseInt(args[1]), args[2]);
			}
		} catch (Exception e) {
			System.out.println("Error Client: " + e.getMessage());
		}
	}
	
    public static void process(String host, int port, String message) {
		try {
			Socket socket = new Socket(host, port);
			NetWriter out = new NetWriter(socket);
			out.putLine(message);
			out.close();
		} catch (Exception e) {
			System.out.println("Error Client: " + e.getMessage());
		}
    }
}
