package view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import model.message.Message;
import model.message.TextMessage;
import util.MessageStatus;
import util.MessageType;

public class MessageBox extends HBox{
	private MessageBubble messageBubble;
	private int messageId;
	private MessageType messageType;
	private MessageStatus messageStatus;
	
	//这可能不是一个好的做法，界面不应该 model 起太大联系……不过暂时先这样吧
	public MessageBox(Message message, MessageType messageType) {
		this.messageType = messageType;
		if(message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)message;
			this.messageId = textMessage.getId();
			String text = textMessage.getContent();
			messageBubble = new MessageBubble(text, messageType);
			this.getChildren().add(messageBubble);
			if(messageType == MessageType.SENDED) {
				this.setAlignment(Pos.CENTER_RIGHT);
			}else {
				this.setAlignment(Pos.CENTER_LEFT);
			}
		}
		
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
	
	public void confirm() {
		messageBubble.confirm();
	}
	
	public int getMessageId() {
		return messageId;
	}
	
}
