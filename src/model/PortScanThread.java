package model;

public class PortScanThread extends Thread{
	private PortScanner portScanner;
	private boolean exit = false;
	private static final int SCAN_PERIOD = 100;//每 100 ms 向下一个端口发送心跳包
	private static final int TIMEOUT = 50000;//最多扫描 50s
	public PortScanThread(PortScanner portScanner) {
		this.portScanner = portScanner;
	}
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		long end;
		long cost;
		while(!exit) {
			portScanner.scan();
			try {
				sleep(SCAN_PERIOD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			end = System.currentTimeMillis();
			cost = end - start;
			if(cost >= TIMEOUT) {
				this.exit();
			}
		}
	}
	
	public void exit() {
		exit = true;
	}
	
}
