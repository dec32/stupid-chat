package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

import model.message.HeartbeatMessage;
import model.message.MessageParser;

public class PortScanner {
	
	private DatagramSocket socket;
	private DatagramPacket packet;
	private InetAddress inetAddress;
	private int port;
	private int count = 0;//用于记录当前已经扫描了多少个端口
	
	//向每个端口重复发送多少次心跳包，由于 UDP 比想象中的可靠，这里先设为 1
	private static final int SEND_TIME = 1;
	
	
	public PortScanner(DatagramSocket socket, SocketAddress dest) {
		this.socket = socket;
		this.inetAddress = ((InetSocketAddress)dest).getAddress();
		this.port = ((InetSocketAddress)dest).getPort();
		//初始化心跳包
		String json = MessageParser.toJson(HeartbeatMessage.getInstance(dest));	
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
		packet = new DatagramPacket(bytes, bytes.length);
		
	}
	
	//往目的 IP 的当前端口发送一个心跳包，然后更新当前端口
	public void scan() {	
		packet.setAddress(inetAddress);
		packet.setPort(port);
		for (int i = 0; i < SEND_TIME; i++) {
			try {
				socket.send(packet);
			} catch (IOException e) {
				// do nothing
			}
			System.out.println("now scanning port: " + port);
		}
		count++;
		port = nextPort(port);
	}
	
	/*
	 * 一般情况，下一次端口 = 本次端口 + 1
	 * 但有一些例外，比如说，当尝试了超过 250 个端口时，就直接回到 1024 (并且重新计数)
	 * 超过 65535 时也回到 1024
	 */
	
	/*
	 * 在给定端口的左右反复试探
	 * 所以，第一次是 +1 第二次是 -2 第三次是 +3 第4次是 -4
	 */
	private int nextPort(int port) {
//		port++;
//		if(count >= 250 ||port >= 65535) {
//			port = 1024;
//			count = 0;
//		}
//		return port;
		if(count%2 == 1) {
			port+=count;
		}else {
			port-=count;
		}
		if(port > 65535 || port <= 0) {
			port = 1024;
		}
		return port;
	}
}
