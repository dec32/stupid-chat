package model.message;

import java.net.SocketAddress;


public class AcknowledgeMessage extends Message{
	private int id;
	public AcknowledgeMessage(SocketAddress socketAddress, int id) {
		super(socketAddress);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
