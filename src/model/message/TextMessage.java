package model.message;

import java.net.SocketAddress;
import java.util.Random;

public class TextMessage extends Message{
	private String content;
	private int id;
	
	public TextMessage(SocketAddress socketAddress, String content, int id) {
		super(socketAddress);
		this.content = content;
		this.id = id;
	}
	
	public TextMessage(SocketAddress socketAddress, String content) {
		super(socketAddress);
		this.content = content;
		this.id = Math.abs(new Random().nextInt());
	}

	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}
	
	
}
