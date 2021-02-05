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
import java.util.Random;

import com.google.gson.JsonObject;

public class Sender {
	private DatagramSocket socket;	
	
	
	public Sender(DatagramSocket socket) {
		this.socket = socket;
	}
	
	public void sendMessage(String msg, SocketAddress socketAddress) {
		JsonObject json = new JsonObject();
		json.addProperty("type", "text");
		json.addProperty("content", msg);
		json.addProperty("id", new Random().nextInt(8192));//生成一个随机数作为消息的 id
		try {
			byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			packet.setSocketAddress(socketAddress);
			socket.send(packet);
			System.out.printf("Message sended: %s\n", msg);
		} catch (UnknownHostException e) {
			System.out.println("Failed to send message: " + msg);
		} catch (IOException e) {
			System.out.println("Failed to send message: " + msg);
		}
	}
	
	public void acknowledge(int id,InetSocketAddress sourceSocketAddress) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", "ack");
		jsonObject.addProperty("id", id);
		
		byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
		packet.setSocketAddress(sourceSocketAddress);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("Failed to acknowledged: " + id);
		}
		System.out.printf("Acknowledged: %d\n", id);
	}
}
