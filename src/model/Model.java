package model;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import model.message.AcknowledgeMessage;
import model.message.HeartbeatMessage;
import model.message.Message;
import model.message.TextMessage;
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
		receiveThread = new ReceiveThread(this);
		receiveThread.start();
		punchThread = new PunchThread(holePuncher);
		punchThread.start();
		view.init(publicSocketAddress);
	}
	
	public void exit() {
		//TODO 这里的方法可以被调用，但不知道为什么关闭窗口时无法关闭程序
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
		TextMessage textMessage = new TextMessage(socketAddress, msg);
		sender.send(textMessage);
	}
	
	public void send(Message message) {
		sender.send(message);
	}
	
	//利用receiver获取一条消息，并且解析	
	public void receive() {
		Message message = receiver.receive();
		if(message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)message;
			String text = textMessage.getContent();
			Platform.runLater(()->{			
//				view.displayMessage(text);
				view.displayMessage(message);
			});	
			AcknowledgeMessage acknowledgeMessage = new AcknowledgeMessage(textMessage.getSocketAddress(), textMessage.getId());
			sender.send(acknowledgeMessage);
		}else if(message instanceof AcknowledgeMessage) {
			//TODO 收到确认报文该怎么办？叫View去更新
			AcknowledgeMessage acknowledgeMessage = (AcknowledgeMessage)message;
			view.confirmMessage(acknowledgeMessage.getId());
		}else if(message instanceof HeartbeatMessage) {
			//kinda do nothing, would add something here later, something like update the online/offline status of the person who send the packet
			view.heartbeat();
		}
	}
}



