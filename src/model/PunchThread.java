package model;

public class PunchThread extends Thread{
	private HolePuncher holePuncher;
	private boolean exit = false;
	public PunchThread(HolePuncher holePuncher) {
		this.holePuncher = holePuncher;
	}
	public void run() {
		while(!exit) {
			holePuncher.punch();
		}
	}

	public void exit() {
		exit = true;
	}
}
