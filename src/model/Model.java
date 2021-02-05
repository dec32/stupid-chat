package model;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import view.View;

public class Model {
	private View view;
	private DatagramSocket socket;
	private Stunner stunner;
	private Sender sender;
	private Receiver receiver;
	private ReceiveThread receiveThread;
	private HolePuncher holePuncher;
	private PunchThread punchThread;	
	private InetSocketAddress publicSocketAddress;
		
	
	// to initialize
	public void setView(View view) {
		this.view = view;
	}

	public Model() {
		try {
			//TODO: 并不是所有功能都需要 socket 提供超时功能
			socket = new DatagramSocket(0);
			socket.setSoTimeout(3000);
		} catch (SocketException e) {
			socket = null;
		}
		stunner = new Stunner(socket);
		sender = new Sender(socket);	
		receiver = new Receiver(socket);	
		holePuncher = new HolePuncher(socket);
	}
	
	//通过 STUN 服务器获取自己的公网地址和端口，初始化消息接收线程和打洞线程
	public void launch() {
		publicSocketAddress = stunner.stun();
//		receiveThread = new ReceiveThread(view, receiver);
		receiveThread = new ReceiveThread(this);
		receiveThread.start();
		punchThread = new PunchThread(holePuncher);
		punchThread.start();
		view.init(publicSocketAddress);
	}
	
	public void exit() {
		receiveThread.exit();
		punchThread.exit();
	}
		
	// 除了启动一个聊天窗口之外，还需要立即向目标发送一个打洞报文，并且让  hole puncher 持续打洞
	public void startChat(InetSocketAddress inetSocketAddress) {
		view.startChat(inetSocketAddress);
		holePuncher.punch(inetSocketAddress);
		holePuncher.addDesitination(inetSocketAddress);
	}
	
	public void send(String msg, SocketAddress socketAddress) {
		sender.sendMessage(msg, socketAddress);
	}
	
	//利用receiver获取一条消息，并且解析
//	public void receive() {
//		String jsonString = receiver.receive();
//		JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
//		String type = jsonObject.get("type").getAsString();
//		if("message".equals(type)) {
//			//TODO 回复确认报文
//			String msg = jsonObject.get("content").getAsString();
//			//委托图形线程更新组件
//			Platform.runLater(()->{			
//				view.displayMessage(msg);
//			});	
//			int id = jsonObject.get("id").getAsInt();
//			jsonObject = new JsonObject();
//			jsonObject.addProperty("type", "ack");
//			jsonObject.addProperty("id", id);
//			//把确认报文发回去
//		}else if("heartbeat".equals(type)) {
//			//kinda do nothing, would add something here later, something like update the online/offline status of the person who send the packet
//		}else if("ack".equals(type)) {
//			//TODO 收到确认报文该怎么办？叫View去更新
//		}
//	}		
	
	
	
	public void receive() {
		Pack pack = receiver.receive();
		String type = pack.getType();
		if("text".equals(type)) {
			//TODO 回复确认报文
			String msg = pack.getString("content");
			//委托图形线程更新组件
			Platform.runLater(()->{			
				view.displayMessage(msg);
			});	
			//把确认报文发回去
			sender.acknowledge(pack.getInt("id"), pack.getSourceSocketAddress());		
		}else if("heartbeat".equals(type)) {
			//kinda do nothing, would add something here later, something like update the online/offline status of the person who send the packet
		}else if("ack".equals(type)) {
			//TODO 收到确认报文该怎么办？叫View去更新
			view.confirmMessage(pack.getInt("id"));
			
		}
	}
}



