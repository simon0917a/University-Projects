package tut3;

import java.net.*;
import java.io.*;

public class Acceptor {

// An application that accepts a connection and receives a message 
//  using stream-mode socket.
// Two command line arguments are expected, in order: 
//    <port number for the Server socket used in this process>
//    <message, a string, to send>

   public static void main(String[] args) {
      if (args.length != 2)
         System.out.println("This program requires 2 command line arguments");
      else {
         try {
            int portNo = Integer.parseInt(args[0]);
            String message = args[1];
            // instantiates a socket for accepting connection   
            ServerSocket connectionSocket = new ServerSocket(portNo);
            System.out.println("now ready for accepting a connection");  
            // wait to accept a connecion request, at which
            //  time a data socket is created                  
            Socket dataSocket = connectionSocket.accept();
            System.out.println("connection accepted"); 
            // get an output stream for writing to the data socket
            OutputStream outStream = dataSocket.getOutputStream();
            // create a PrinterWriter object for character-mode output
            PrintWriter socketOutput = new PrintWriter(new OutputStreamWriter(outStream));
            // write a message into the data stream
            socketOutput.println(message);
            //The ensuring flush method call is necessary for the data to
            // be written to the socket data stream before the
            // socket is closed.
            socketOutput.flush();
            System.out.println("message sent"); 
            dataSocket.close( );
            System.out.println("data socket closed");
            connectionSocket.close( );
            System.out.println("connection socket closed"); 
         } // end try
          catch (Exception ex) {
            ex.printStackTrace( );
          } //end catch
      } // end else
   } // end main
} // end class
