package model;

public class ReceiveThread extends Thread{
	private Model model;
	public ReceiveThread(Model model) {
		this.model = model;
	}
	public void run() {
		System.out.println("Message receiving thread started.");
		while(true) {
			//这里会阻塞 所以导致线程无法停止
			if(this.isInterrupted()) {
				break;
			}
			model.receive();
		}
		System.out.println("Message receiving thread stoped.");
	}

}
