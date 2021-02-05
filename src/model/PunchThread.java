package model;

public class PunchThread extends Thread{
	private HolePuncher holePuncher;
	private boolean exit = false;
	public PunchThread(HolePuncher holePuncher) {
		this.holePuncher = holePuncher;
	}
	public void run() {
		System.out.println("Hole punching thread started.");
		while(!exit) {
			holePuncher.punch();
		}
		System.out.println("Hole punching thread stoped.");
	}

	public void exit() {
		exit = true;
	}
}
