package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import de.javawi.jstun.attribute.MappedAddress;
import de.javawi.jstun.attribute.MessageAttributeInterface.MessageAttributeType;
import de.javawi.jstun.attribute.MessageAttributeParsingException;
import de.javawi.jstun.header.MessageHeader;
import de.javawi.jstun.header.MessageHeaderInterface.MessageHeaderType;
import de.javawi.jstun.header.MessageHeaderParsingException;
import de.javawi.jstun.util.UtilityException;

public class Stunner {
	private DatagramSocket socket = null;
	private static int STUN_RETRY_TIMES = 10;
	
	public Stunner(DatagramSocket socket) {
		this.socket = socket;
	}
	
	
	public InetSocketAddress stun() {
		InetSocketAddress publicSocketAddress = null;
		for (int i = 0; i < STUN_RETRY_TIMES; i++) {
			publicSocketAddress = tryStun();
			if(publicSocketAddress != null) {
				break;
			}
			System.out.println("返回值为空");
		}
		return publicSocketAddress;
	}
	
	
	public InetSocketAddress tryStun() {
		//发送 STUN 报文
		try {
			MessageHeader mh = new MessageHeader(MessageHeaderType.BindingRequest);
			mh.generateTransactionID();
			byte[] data = mh.getBytes();	
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("stun.voip.aebc.com"), 3478);
			socket.send(packet);
		} catch (Exception e) {
			System.out.println("发送STUN报文失败");
			return null;
		}
		
		//等待 STUN 响应
		try {
			byte[] buf = new byte[200];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			byte[] data = packet.getData();
			MessageHeader mh = new MessageHeader();
			mh = MessageHeader.parseHeader(data);
			mh.parseAttributes(data);
			MappedAddress ma = (MappedAddress) mh.getMessageAttribute(MessageAttributeType.MappedAddress);
			String ip = ma.getAddress().getInetAddress().toString().split("/")[1];
			int port = ma.getPort();
			System.out.println("公网地址/端口：" + ip + ":" + port);
			return new InetSocketAddress(ip, port);
		} catch (SocketTimeoutException e) {
			System.out.println("等待 STUN 回复超时");
			return null;
		} catch (IOException e) {
			System.out.println("接收 STUN 报文遇到异常");
			return null;
		} catch (MessageHeaderParsingException e) {
			System.out.println("无法解析 STUN 报文");
			return null;
		} catch (MessageAttributeParsingException e) {
			System.out.println("无法解析 STUN 报文");
			return null;
		} catch (UtilityException e) {
			System.out.println("接收 STUN 报文遇到异常");
			return null;
		}		
	}
	
//	public boolean tryStun(){
//		try {
//			sendStunRequest();
//			recieveStunResponse();
//		} catch (SocketTimeoutException e) {
//			System.out.println("STUN 请求超时");
//			return false;
//			
//		} catch (UtilityException e) {
//			System.out.println("STUN 遇到错误");
//		} catch (IOException e) {
//			System.out.println("STUN 遇到错误");
//		} catch (MessageHeaderParsingException e) {
//			System.out.println("STUN 遇到错误");
//		} catch (MessageAttributeParsingException e) {
//			System.out.println("STUN 遇到错误");
//		}
//		System.out.println("公网地址/端口：" + publicSocketAddress.getAddress() + ":" + publicSocketAddress.getPort());
//		return true;
//	}
	
	
	
	
//	private void sendStunRequest() throws UtilityException, IOException {
//		MessageHeader mh = new MessageHeader(MessageHeaderType.BindingRequest);
//		mh.generateTransactionID();
//		byte[] data = mh.getBytes();	
//		DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("stun.voip.aebc.com"), 3478);
//		socket.send(packet);
//	}
//	
//	private void recieveStunResponse() throws IOException, MessageHeaderParsingException, MessageAttributeParsingException, UtilityException {
//		byte[] buf = new byte[200];
//		DatagramPacket packet = new DatagramPacket(buf, buf.length);
//		socket.setSoTimeout(3000);
//		try {
//			socket.receive(packet);
//		} catch (SocketTimeoutException e) {
//			System.out.println("等待 STUN 回复超时");
//			return;
//		}
//		
//		byte[] data = packet.getData();
//		MessageHeader mh = new MessageHeader();
//		mh = MessageHeader.parseHeader(data);
//		mh.parseAttributes(data);
//		MappedAddress ma = (MappedAddress) mh.getMessageAttribute(MessageAttributeType.MappedAddress);
//		String ip = ma.getAddress().getInetAddress().toString().split("/")[1];
//		int port = ma.getPort();
//		this.publicSocketAddress = new InetSocketAddress(ip, port);
//		
//	}	
	
}
