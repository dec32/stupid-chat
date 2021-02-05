package model.message;

import java.net.SocketAddress;

public class HeartbeatMessage extends Message{
	public HeartbeatMessage(SocketAddress socketAddress) {
		super(socketAddress);
	}
}
