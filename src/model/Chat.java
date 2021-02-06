package model;

import java.net.SocketAddress;

import model.message.AcknowledgeMessage;
import model.message.TextMessage;
import view.View;

/*
 * TODO: CHAT类用来维护一个聊天，记录收到的和发出的消息以及他们状态
 * 这才是mvc架构中 model 的作用，chatwindow中的一些功能需要被移植到这里来
 */

public class Chat {
	private Sender sender;
	private View view;
	private SocketAddress socketAddress;
	
	public void send(String text) {
		sender.send(new TextMessage(socketAddress, text));
	}
	
	public void confirm(AcknowledgeMessage acknowledgeMessage) {
		
	}

}
