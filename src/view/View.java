package view;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import controller.Controller;
import model.Model;

public class View {
	private Controller controller;
	private Model model;
	
	private InitWindow initWindow;
	private ChatWindow chatWindow;
	
	//to initialize
	public void setController(Controller controller) {
		this.controller = controller;
	}
	public void setModel(Model model) {
		this.model = model;
	}


	
	public void init(InetSocketAddress publicSocketAddress) {
		initWindow = new InitWindow(controller,publicSocketAddress);
		initWindow.show();
	}
	
	public void startChat(SocketAddress socketAddress) {
		chatWindow = new ChatWindow(controller, socketAddress);
		chatWindow.show();
		initWindow.close();
	}
	
	public void displayMessage(String msg) {
		chatWindow.displayMessage(msg);
	}

	
	
	
	
}
