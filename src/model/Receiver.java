package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import model.message.Message;
import model.message.MessageParser;

public class Receiver{
	private DatagramSocket socket;
	private boolean exit = false;

	public Receiver(DatagramSocket socket) {
		this.socket = socket;
	}
	
	/*
	 * 直到收到有效的包之前，一直循环
	 * 如果收到了有效的包，就跳出循环，返回文本内容
	 */
	
	public Message receive() {
		byte[] buf = new byte[200];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(packet);
			} catch (SocketTimeoutException e) {
				if(!exit) {
					continue;
				}
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		}
		SocketAddress socketAddress = packet.getSocketAddress();
		String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
		if(!"{}".equals(json)) {
			System.out.println("Message recieved: " + json);
		}	
		return MessageParser.parse(json, socketAddress);
	}
	
	public void exit() {
		exit = true;
	}
	
	
	
}

