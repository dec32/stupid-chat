package model;

public class HeartbeatThread extends Thread{
	private Heartbeater heartbeater;
	private boolean exit = false;
	private static final int BEAT_PERIOD = 250;//每1秒进行一次 punch
	public HeartbeatThread(Heartbeater heartbeater) {
		this.heartbeater = heartbeater;
	}
	
	@Override
	public void run() {
		System.out.println("Heartbeat thread started.");
		while(!exit) {
			heartbeater.heartbeat();
			try {
				sleep(BEAT_PERIOD);
			} catch (InterruptedException e) {
				return;
			}
		}
		System.out.println("Heartbeat thread stoped.");
		
//		while(true) {
//			if(this.isInterrupted()) {
//				return;
//			}
//			heartbeater.heartbeat();
//			try {
//				sleep(BEAT_PERIOD);
//			} catch (InterruptedException e) {
//				return;
//			}
//		}
		
	}
	
	public void exit() {
		exit = true;
	}
	
}
