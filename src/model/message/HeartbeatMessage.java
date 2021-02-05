package model.message;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class HeartbeatMessage extends Message{
	private static Map<SocketAddress, HeartbeatMessage> heartbeatMessageMap = new HashMap<SocketAddress, HeartbeatMessage>();
	
	private HeartbeatMessage(SocketAddress socketAddress) {
		super(socketAddress);
	}
	
	public static HeartbeatMessage getInstance(SocketAddress socketAddress) {
		if(heartbeatMessageMap.containsKey(socketAddress)) {
			return heartbeatMessageMap.get(socketAddress);
		}
		HeartbeatMessage heartbeatMessage = new HeartbeatMessage(socketAddress);
		heartbeatMessageMap.put(socketAddress, heartbeatMessage);
		return heartbeatMessage;
	}
}
