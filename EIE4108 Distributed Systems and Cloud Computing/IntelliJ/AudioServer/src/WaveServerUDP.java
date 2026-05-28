import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

/*
 * Example usage: java WaveServerUDP ../news.wav localhost 1111 2222
 */
public class WaveServerUDP {
	public static void main(String args[]) {
		if (args.length != 4) {
			System.out.println("Usage: java WaveServerUDP <wavefile> <clientHost> <serverAckPort> <clientAudioPort>");
			return;
		}
		File waveFile = new File(args[0]);
		int serverAckPort = Integer.parseInt(args[2]);
		int clientAudioPort = Integer.parseInt(args[3]);
		int bufSize = 1600; // 50ms for 16kHz, 16bits/sample
		try {
			InetAddress clientHost = InetAddress.getByName(args[1]);
			AudioInputStream ais = AudioSystem.getAudioInputStream(waveFile);

			// Create datagram socket (serverAudioSocket) for sending audio data
			// Put your code here
			DatagramSocket serverAudioSocket = new DatagramSocket();

			byte[] soundData = new byte[bufSize];						
			// Create a datagram packet (audioPacket) for storing audio data in array soundData[]
			// Put your code here
			DatagramPacket audioPacket = new DatagramPacket(soundData, soundData.length, clientHost, clientAudioPort);
			
			// Create a datagram socket (serverAckSocket) for receiving acknowledgment from WaveClientUDP
			// Put your code here
			DatagramSocket serverAckSocket = new DatagramSocket(serverAckPort);
			
			byte[] ack = new byte[]{1};
			// Create a datagram packet (ackPacket) for receiving acknowledgment from WaveClientUDP
			// Put your code here
			DatagramPacket ackPacket = new DatagramPacket(ack, ack.length);
			
			// Read the audio file frame-by-frame. For each frame, send the audio data in soundData[]
			// to clientAudioPort of WaveClientUDP. Then, wait for the acknowledgment byte from
			// WaveClientUDP at serverAckPort. Upon receiving the acknowledgment byte, proceed to
			// read the next frame until the end of the audio file. If the acknowledgment byte is 0,
			// terminate the sending process and close the serverAudioSocket and serverAckSocket.
			// Put your code here.
			int bytesRead = 0;
			while ((bytesRead = ais.read(soundData)) != -1) {
				audioPacket.setData(soundData, 0, bytesRead);
				serverAudioSocket.send(audioPacket);
				serverAckSocket.receive(ackPacket);

				if (ack[0] == 0) {
					break;
				}
			}

			serverAudioSocket.close();
			serverAckSocket.close();
			System.out.println("Server finished sending.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}