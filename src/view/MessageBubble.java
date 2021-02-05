package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import util.MessageType;

//TODO: 不应当继承 Label，这样子会导致文字无法选中和复制
public class MessageBubble extends Label{
	private static final double MAX_WIDTH = 300;
	private MessageType messageType;
	public MessageBubble(String text, MessageType messageType) {
		this.setText(text);
		this.messageType = messageType;
		this.setPadding(new Insets(7));
		this.setMaxWidth(MAX_WIDTH);
		this.setWrapText(true);
		if(messageType==MessageType.SENDED) {
			this.setStyle("-fx-background-radius: 10px; -fx-background-color: rgb(179,231,244);");
		}else {
			this.setStyle("-fx-background-radius: 10px; -fx-background-color: rgb(220,220,220);");
		}
	}
	public MessageType getMessageType() {
		return messageType;
	}
	
}
