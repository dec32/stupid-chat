package model.message;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Message {
	protected SocketAddress socketAddress;
	public Message(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}
	public SocketAddress getSocketAddress() {
		return socketAddress;
	}
}
