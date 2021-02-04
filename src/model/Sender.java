package model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Sender {
	private DatagramSocket socket;	
	
	
	public Sender(DatagramSocket socket) {
		this.socket = socket;
	}


	public void sendMessage(String msg, String remoteHost, int remotePort) {
		try {
			byte[] bytes = msg.getBytes("UTF-8");
			DatagramPacket packet = new DatagramPacket(
				bytes, 
				bytes.length,
				InetAddress.getByName(remoteHost), 
				remotePort
			);
			socket.send(packet);
			System.out.printf("ÎÒ£º%s\n", msg);
		} catch (UnsupportedEncodingException e) {
			System.out.println("·¢ËÍÊ§°Ü");
		} catch (UnknownHostException e) {
			System.out.println("·¢ËÍÊ§°Ü");
		} catch (IOException e) {
			System.out.println("·¢ËÍÊ§°Ü");
		}
	}
	
	public void sendMessage(String msg, SocketAddress socketAddress) {
		try {
			byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			packet.setSocketAddress(socketAddress);
			socket.send(packet);
			System.out.printf("ÎÒ£º%s\n", msg);
		} catch (UnknownHostException e) {
			System.out.println("·¢ËÍÊ§°Ü");
		} catch (IOException e) {
			System.out.println("·¢ËÍÊ§°Ü");
		}
	}
}
