package controller;

import java.net.SocketAddress;

import model.Model;
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

	public void send(String msg, SocketAddress socketAddress) {
		model.send(msg, socketAddress);
	}
	
	public void startChat(SocketAddress socketAddress) {
		view.startChat(socketAddress);
	}
	
	public void exit() {
		model.exit();
	}
	
}
