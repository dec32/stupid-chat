package model;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

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
	
	public void launch() {
		publicSocketAddress = stunner.stun();
		receiveThread = new ReceiveThread(view, receiver);
		receiveThread.start();		
		holePuncher.launch();
		view.init(publicSocketAddress);
	}
	
	public void exit() {
		receiveThread.exit();
		holePuncher.exit();
	}
	
	
	public void send(String msg, String remoteHost, int remotePort) {
		sender.sendMessage(msg, remoteHost, remotePort);
	}
	
	public void send(String msg, SocketAddress socketAddress) {
		sender.sendMessage(msg, socketAddress);
	}
		
}

class ReceiveThread extends Thread {
	private View view;
	private Receiver receiver;
	private boolean exit = false;

	public ReceiveThread(View view, Receiver receiver) {
		this.view = view;
		this.receiver = receiver;
	}

	public void exit() {
		this.exit = true;
	}

	@Override
	public void run() {
		System.out.println("消息接收线程启动");
		while (!exit) {
			String msg = receiver.receive();
			//委托图形线程更新组件
			Platform.runLater(()->{
				view.displayMessage(msg);
			});			
		}
		System.out.println("消息接收线程终止");
	}
}


