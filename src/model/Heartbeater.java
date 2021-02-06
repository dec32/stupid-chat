package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;


import model.message.HeartbeatMessage;
import model.message.MessageParser;

/*
 * 虽然这东西叫做 puncher 但他的功能主要是真正意义上的心跳和防止端口老化
 * 之后再想想改成什么名字吧……
 */
public class Heartbeater{
	private DatagramSocket socket;
	private DatagramPacket packet;
	private SocketAddress dest = null;
	
	
	
	public Heartbeater(DatagramSocket socket) {
		this.socket = socket;
		//初始化心跳包
		String json = MessageParser.toJson(HeartbeatMessage.getInstance(dest));	
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
		packet = new DatagramPacket(bytes, bytes.length);
	}


	//向指定目标端口发送心跳包
	public void heartbeat() {
		if(dest == null) {
			return;
		}
		packet.setSocketAddress(dest);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("Failed to send hole-punching packet.");
		}
	}
	
	//设置目标
	public void setDestination(SocketAddress dest) {
		this.dest = dest;
	}
}
