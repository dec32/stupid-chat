package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class Receiver{
	private DatagramSocket socket;

	public Receiver(DatagramSocket socket) {
		this.socket = socket;
	}
	
	/*
	 * 直到收到有效的包之前，一直循环
	 * 如果收到了有效的包，就跳出循环，返回文本内容
	 */
	public String receive() {
		byte[] buf = new byte[200];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(packet);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				continue;
			}
			break;
		}
		//TODO：是时候开始考虑设计 message 类了
		String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);		
		System.out.println("对方: " + msg);
		return msg;
	}
	
	
}

