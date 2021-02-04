package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HolePuncher{
	private DatagramSocket socket;
	private DatagramPacket packet;
	private List<SocketAddress> socketAddressList = new ArrayList<SocketAddress>();
	
	public HolePuncher(DatagramSocket socket) {
		this.socket = socket;
		String punchMsg = "hole-punching";
		byte[] bytes = punchMsg.getBytes(StandardCharsets.UTF_8);
		packet = new DatagramPacket(bytes, bytes.length);
	}


	//向指定端口发送打洞报文
	private void punch(SocketAddress socketAddress) {
		packet.setSocketAddress(socketAddress);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("打洞报文发送失败。");
		}
	}
	
	public void launch() {
		
	}
	
	public void exit() {
		
	}
	
	private void punchAll() {
		for (SocketAddress sa:socketAddressList) {
			punch(sa);
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// do nothing
		}
	}
	
	public void addDesitination(SocketAddress socketAddress) {
		socketAddressList.add(socketAddress);
	}
}
