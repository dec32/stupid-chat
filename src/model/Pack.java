package model;

import java.net.InetSocketAddress;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Pack {
	private InetSocketAddress sourceSocketAddress;
	private JsonObject jsonObject;
		
	public Pack(InetSocketAddress sourceSocketAddress, String json) {
		this.sourceSocketAddress = sourceSocketAddress;
		this.jsonObject = JsonParser.parseString(json).getAsJsonObject();
	}
	
	public InetSocketAddress getSourceSocketAddress() {
		return sourceSocketAddress;
	}

	public String getType() {
		return jsonObject.get("type").getAsString();
	}
	public String getString(String memberName) {
		return jsonObject.get(memberName).getAsString();
	}
	public int getInt(String memberName) {
		return jsonObject.get(memberName).getAsInt();
	}
	
}