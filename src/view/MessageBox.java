package view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import util.MessageStatus;
import util.MessageType;

public class MessageBox extends HBox{
	private int id;
	private String text;
	private MessageType messageType;
	private MessageStatus messageStatus;
	
	public MessageBox(String text,String id) {
		
	}
	
	//TODO: 这个类应该是唯一一个给外界去 new 的类，所以要重写构造方法，自己把气泡什么的搞定
	public MessageBox(MessageBubble messageBubble) {
		if(messageBubble.getMessageType()==MessageType.SENDED) {
			this.getChildren().add(messageBubble);
			this.setAlignment(Pos.CENTER_RIGHT);
		}else {
			this.getChildren().add(messageBubble);
			this.setAlignment(Pos.CENTER_LEFT);
		}
		
	}
}
