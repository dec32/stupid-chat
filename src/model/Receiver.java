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

	public Receiver(DatagramSocket socket) {
		this.socket = socket;
	}
	
	/*
	 * 直到收到有效的包之前，一直循环
	 * 如果收到了有效的包，就跳出循环，返回文本内容
	 * TODO: 没有收到消息就一直阻塞，这不是一个好的设计
	 * 应当在每次超时的时候，向调用者抛出超时异常，然后由调用者决定要不要继续receive
	 */
	
	public Message receive() throws SocketTimeoutException {
		byte[] buf = new byte[200];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
		} catch(SocketTimeoutException e) {
			throw e;
		} catch (IOException e) {
			//TODO 之后再想想这里该干嘛
			e.printStackTrace();
		}
		SocketAddress socketAddress = packet.getSocketAddress();
		String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
		if(!"{}".equals(json)) {
			System.out.println("Message recieved: " + json);
		}	
		return MessageParser.parse(json, socketAddress);
	}
	
}

