package model.message;

import java.net.SocketAddress;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/*
 * 大量胶水代码
 * 想办法重写，尽管这很难
 */
public class MessageParser {
	public static Message parese(String json, SocketAddress socketAddress) {
		JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		String type = jsonObject.get("type").getAsString();
		if("text".equals(type)) {
			String content = jsonObject.get("conent").getAsString();
			int id = jsonObject.get("id").getAsInt();
			return new TextMessage(socketAddress, content, id);
		}else if("ack".equals(type)) {
			int id = jsonObject.get("id").getAsInt();
			return new AcknowledgeMessage(socketAddress, id);
		}else if("heartbeat".equals(type)) {
			return new HeartbeatMessage(socketAddress);
		}
		return null;
	}
	
	public static String toJson(TextMessage textMessage) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", "text");
		jsonObject.addProperty("content", textMessage.getContent());
		jsonObject.addProperty("id", textMessage.getId());
		return jsonObject.toString();	
	}
	
	public static String toJson(AcknowledgeMessage acknowledgeMessage) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", "ack");
		jsonObject.addProperty("id", acknowledgeMessage.getId());
		return jsonObject.toString();	
	}
	
	
}
