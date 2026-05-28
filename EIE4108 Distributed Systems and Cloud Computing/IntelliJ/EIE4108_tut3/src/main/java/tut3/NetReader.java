package tut3;

import java.lang.*;
import java.net.*;
import java.io.*;

class NetReader {
    public final static String EOF = "\rEOF";
    private BufferedInputStream m_in;
    private String m_message = null;

    public NetReader(Socket s) {
		try {
			BufferedInputStream in = new BufferedInputStream(s.getInputStream());
	        m_in = in;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }

    public String getLine() {
		try {
			String res = "";
			while (true) {
				int c = m_in.read();
				if (c == '\n') return res;
				if (c == -1) return EOF;
				if (c != '\r') {
					res += (char) c;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return EOF;
    }
    
    public void close() {
		try {
			m_in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
}
