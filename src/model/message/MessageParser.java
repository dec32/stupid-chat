package model.message;

import java.net.SocketAddress;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/*
 * 大量胶水代码，当消息种类增加时，这里会变得很难看
 * 想办法重写，尽管这很难
 */
public class MessageParser {
	
	
	public static Message parse(String json, SocketAddress socketAddress) {
		JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		if(jsonObject.get("type") == null) {
			//type项为空，说明为“{}”或者别的什么东西，视作心跳包
			return HeartbeatMessage.getInstance(socketAddress);
		}
		String type = jsonObject.get("type").getAsString();
		if("text".equals(type)) {
			String content = jsonObject.get("content").getAsString();
			int id = jsonObject.get("id").getAsInt();
			return new TextMessage(socketAddress, content, id);
		}else if("ack".equals(type)) {
			int id = jsonObject.get("id").getAsInt();
			return new AcknowledgeMessage(socketAddress, id);
		}else if("heartbeat".equals(type)) {
//			心跳包应当使用单例模式，不然需要 new 的对象实在太多了
			return HeartbeatMessage.getInstance(socketAddress);
//		}else if(type == null) {
//			return HeartbeatMessage.getInstance(socketAddress);
		}
		return null;
	}
	
	public static String toJson(Message message) {
		JsonObject jsonObject = new JsonObject();
		if(message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)message;
			jsonObject.addProperty("type", "text");
			jsonObject.addProperty("content", textMessage.getContent());
			jsonObject.addProperty("id", textMessage.getId());		
		}else if(message instanceof AcknowledgeMessage) {
			AcknowledgeMessage acknowledgeMessage = (AcknowledgeMessage)message;
			jsonObject.addProperty("type", "ack");
			jsonObject.addProperty("id", acknowledgeMessage.getId());
		}else if(message instanceof HeartbeatMessage) {
//			jsonObject.addProperty("type", "heartbeat");
			return "{}";
			
		}
		return jsonObject.toString();
	}
	
	
	
}
