package model;

public class PortScanThread extends Thread{
	private PortScanner portScanner;
	private static final int SCAN_PERIOD = 50;//每 250 ms 向下一个端口发送心跳包
	private static final int MAX_PORT_COUNT = 4000;//最多扫描 4000 个端口
	public PortScanThread(PortScanner portScanner) {
		this.portScanner = portScanner;
	}
	@Override
	public void run() {
		//启动之前先留出 3s 的缓冲时间，没有什么特殊的原因，只是为了方便本机测试，
		System.out.println("Port scanning thread started.");
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Port scanning thread stoped.");
			return;
		}
		long start = System.currentTimeMillis();
		long end;
		long cost;
		int portCount = 0;
		//TODO 这么频繁的 sleep 不是什么好设计，应该一次性扫描几十个端口，然后再睡一下，而不是扫一个睡一个
		while(true) {
			portScanner.scan();
			portCount++;
			try {
				sleep(SCAN_PERIOD);
			} catch (InterruptedException e) {
				System.out.println("Port scanning thread stoped.");
				return;
			}
			end = System.currentTimeMillis();
			cost = end - start;
			if(portCount >= MAX_PORT_COUNT) {
				//当扫描的端口数超过了设定的阈值时，强制终止
				break;
			}
		}
	}
	

	
}
