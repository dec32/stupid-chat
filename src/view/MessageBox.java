package view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class MessageBox extends HBox{
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
