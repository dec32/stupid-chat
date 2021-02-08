package model;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

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
	private InetSocketAddress publicSocketAddress;
	
	private Stunner stunner;
	private Sender sender;
	private Receiver receiver;	
	private PortScanner portScanner;
	private Heartbeater heartbeater;
		
	private ReceiveThread receiveThread;
	private PortScanThread portScanThread;
	private HeartbeatThread heartbeatThread;
	// to initialize
	public void setView(View view) {
		this.view = view;
	}

	public Model() {
		try {
			//TODO: 并不是所有功能都需要 socket 提供超时功能
			socket = new DatagramSocket(0);
			socket.setSoTimeout(1000);
		} catch (SocketException e) {
			socket = null;
		}
		stunner = new Stunner(socket);
		sender = new Sender(socket);	
		receiver = new Receiver(socket);	
		heartbeater = new Heartbeater(socket);
	}
	
	
	public void launch() {
		/*
		 * 利用 STUN 获取自己的公网 ip 和端口以后，启动消息接收线程和心跳线程
		 * 在一开始，心跳线程并没有设定目的地
		 * 在用户输入了对方的 ip 地址和端口号以后，就会间接调用 startChat 方法
		 */
		publicSocketAddress = stunner.stun();
		receiveThread = new ReceiveThread(this);
		receiveThread.start();
		
		view.init(publicSocketAddress);
	}
	
	public void exit() {
		if(receiveThread != null) {
			receiveThread.interrupt();
		}
		if(heartbeatThread != null) {
			heartbeatThread.interrupt();
		}
		
		
	}
		
	// 除了启动一个聊天窗口之外，还需要立即向目标发送一个打洞报文，并且让  hole puncher 持续打洞
	public void startChat(InetSocketAddress inetSocketAddress) {
		/*
		 * 启动聊天，第一件事情就是启动心跳线程，不断地向目的地发送心跳包
		 * 当然，由于对称型 NAT 的存在，这一目的地可能根本就是无效的
		 * 真实的目的地需要等对方告知我们，但是，在此之前，我们需要先猜中对方的端口，并且发送一个心跳包过去
		 * 只有发送了心跳包以后，来自对方真实端口的报文才有办法到达我们的端口
		 * 所以需要开启一个十分疯狂的端口扫描线程
		 */
		
		heartbeatThread = new HeartbeatThread(heartbeater);
		heartbeater.setDestination(inetSocketAddress);
		heartbeatThread.start();
		view.startChat(inetSocketAddress);
		
		//开启端口扫描线程
		portScanner = new PortScanner(socket, inetSocketAddress);
		portScanThread = new PortScanThread(portScanner);
		portScanThread.start();
	}
	
	public void send(Message message) {
		sender.send(message);
	}
	
	/*
	 * 利用receiver获取一条消息，并且解析
	 * TODO: 这一部分的代码应当挪到别的什么地方去，Model 类本身不该处理这么复杂的逻辑
	 */
	public void receive() {
		Message message;
		try {
			message = receiver.receive();
		} catch (SocketTimeoutException e) {
			return;
		}
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
			//收到确认报文该怎么办？叫View去更新
			AcknowledgeMessage acknowledgeMessage = (AcknowledgeMessage)message;
			view.confirmMessage(acknowledgeMessage.getId());
		}else if(message instanceof HeartbeatMessage) {
			//收到心跳包之后，就让 view 的心跳灯闪一下
			view.heartbeat();
			/*
			 * 我要在这里写一些十分可怕的东西：
			 * 只要我在这个地方收到了心跳包，我就更新一次socketAddress
			 * 这样显然是不合理的，第一，这样一来，我就无法同时与多个用户聊天了
			 * 第二，别人可以很轻易的伪造身份
			 * 但无论如何，先这么做吧，让软件跑起来再说
			 */
			view.updateSocketAddress(message.getSocketAddress());		
			heartbeater.setDestination(message.getSocketAddress());
			//关闭端口扫描线程
			if(portScanThread.isAlive()) {
				portScanThread.interrupt();
			}
		}
	}
	
}



