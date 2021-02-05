package model;

public class ReceiveThread extends Thread{
	private Model model;
	private boolean exit = false;
	public ReceiveThread(Model model) {
		this.model = model;
	}
	public void run() {
		while(!exit) {
			model.receive();
		}
	}

	public void exit() {
		exit = true;
	}
}
