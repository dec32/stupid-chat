package controller;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import model.Model;
import model.message.Message;
import view.View;

public class Controller {
	private Model model;
	private View view;
	
	
	// to initialize
	public void setModel(Model model) {
		this.model = model;
	}

	public void setView(View view) {
		this.view = view;
	}
	
	//method to call
	public void send(Message message) {
		model.send(message);
	}
	
	public void startChat(InetSocketAddress inetSocketAddress) {
		model.startChat(inetSocketAddress);
	}
	
	public void exit() {
		model.exit();
	}
	
	
}
