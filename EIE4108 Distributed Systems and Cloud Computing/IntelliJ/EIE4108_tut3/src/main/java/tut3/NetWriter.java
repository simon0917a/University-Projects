package tut3;

import java.lang.*;
import java.net.*;
import java.io.*;

class NetWriter {
    private PrintWriter m_out;
    private String m_message = null;

    public NetWriter(Socket s) {
		try {
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
			PrintWriter out = new PrintWriter(bos);
			m_out = out;
		} catch (Exception e) {
			System.out.println("Error Net_Writer: " + e.getMessage());
		}
    }

    public void putLine(String message) {
		m_out.println(message);  // Send message
		m_out.flush();           // Make sure gone
    }

    public void close() {
		m_out.close();
    }
}
