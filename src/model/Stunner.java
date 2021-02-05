package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.javawi.jstun.attribute.MappedAddress;
import de.javawi.jstun.attribute.MessageAttributeInterface.MessageAttributeType;
import de.javawi.jstun.attribute.MessageAttributeParsingException;
import de.javawi.jstun.header.MessageHeader;
import de.javawi.jstun.header.MessageHeaderInterface.MessageHeaderType;
import de.javawi.jstun.header.MessageHeaderParsingException;
import de.javawi.jstun.util.UtilityException;

public class Stunner {
	private DatagramSocket socket = null;
	private List<String> serverList = new ArrayList<String>();
	private static int STUN_RETRY_TIMES = 10;
	
	public Stunner(DatagramSocket socket) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("res/stun server list.txt"));
			while(true) {
				String line = in.readLine();
				if(line == null) {
					break;
				}
				serverList.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find stun server list.txt");
		} catch (IOException e) {
			System.out.println("Exception occurred while loading stun server list.txt");
		}	
		this.socket = socket;
	}
	
	
	public InetSocketAddress stun() {
		InetSocketAddress publicSocketAddress = null;
		for (int i = 0; i < STUN_RETRY_TIMES; i++) {
			publicSocketAddress = tryStun();
			if(publicSocketAddress != null) {
				break;
			}
		}
		return publicSocketAddress;
	}
	
	
	public InetSocketAddress tryStun() {
		//发送 STUN 报文，从 STUN 服务器列表中随机选择一个，然后发送 STUN 报文
		int index = new Random().nextInt(serverList.size());
		String stunServerIp = serverList.get(index).split(":")[0];
		int stunServerPort = Integer.valueOf(serverList.get(index).split(":")[1]);
		System.out.println("Sending STUN request to " + serverList.get(index));
		try {
			MessageHeader mh = new MessageHeader(MessageHeaderType.BindingRequest);
			mh.generateTransactionID();
			byte[] data = mh.getBytes();	
			DatagramPacket packet = new DatagramPacket(data, data.length);
			SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(stunServerIp),stunServerPort);
			packet.setSocketAddress(socketAddress);
			socket.send(packet);
			
		} catch (Exception e) {
			System.out.println("Failed to send STUN request.");
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
			System.out.println("Public socket address: " + ip + ":" + port);
			return new InetSocketAddress(ip, port);
		} catch (SocketTimeoutException e) {
			System.out.println("No STUN response. Timeout.");
			return null;
		} catch (IOException e) {
			System.out.println("Exception occured while receiving STUN response.");
			return null;
		} catch (MessageHeaderParsingException e) {
			System.out.println("Cannot parse STUN response.");
			return null;
		} catch (MessageAttributeParsingException e) {
			System.out.println("Cannot parse STUN response.");
			return null;
		} catch (UtilityException e) {
			System.out.println("Exception occured while receiving STUN response.");
			return null;
		}		
	}
	
	
}
