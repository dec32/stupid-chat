package view;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatWindow extends Stage{
	private Controller controller;
	private SocketAddress socketAddress;
	
	private VBox msgPane = new VBox();
	private ScrollPane msgScrollPane;
	private TextField typeField = new TextField();
	private Button sendButton = new Button("发送"); 
	
	
	public ChatWindow(Controller controller, SocketAddress socketAddress) {
		this.controller = controller;
		this.socketAddress = socketAddress;
		initUI();
		initListener();
	}
	
	public ChatWindow() {
		initUI();
		initListener();
	}
	
	public void initUI() {
		VBox mainLayout = new VBox();
		
		msgScrollPane = new ScrollPane(msgPane);
		msgScrollPane.setFitToWidth(true);
		msgScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		msgScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		HBox typePane = new HBox(typeField,sendButton);
		mainLayout.getChildren().addAll(msgScrollPane, typePane);
		
		
		VBox.setVgrow(msgScrollPane, Priority.ALWAYS);
		HBox.setHgrow(typeField, Priority.ALWAYS);
		
		msgPane.setPadding(new Insets(10));
		msgPane.setSpacing(5);

		typePane.setPadding(new Insets(10));
		typePane.setSpacing(5);
		this.setWidth(400);
		this.setHeight(400);
		this.setTitle("Stupid Chat");
		this.setScene(new Scene(mainLayout));
		
		MessageBubble sendedMessageBubble = new MessageBubble("发出消息",MessageType.SENDED);
		MessageBox sendedMessageBox = new MessageBox(sendedMessageBubble);
		
		MessageBubble receivedMessageBubble = new MessageBubble("收到消息",MessageType.RECIEVED);
		MessageBox receivedMessageBox = new MessageBox(receivedMessageBubble);
		
		
		msgPane.getChildren().add(sendedMessageBox);
		msgPane.getChildren().add(receivedMessageBox);
	}
	
	public void initListener() {
		sendButton.setOnAction(e->{
			on_send();
		});
		typeField.setOnAction(e->{
			on_send();
		});
		//TODO: 这样子不满足要求，因为我们不希望自己没有在说话而对方发消息过来的时候界面突然被滚动到最下面
		msgPane.heightProperty().addListener(e->{
			msgScrollPane.setVvalue(1.0);
		});
		this.setOnCloseRequest(e->{
			controller.exit();
		});
	}
	
	private void on_send() {
		String msg = typeField.getText();
		typeField.clear();
		msgPane.getChildren().add(new MessageBox(new MessageBubble(msg, MessageType.SENDED)));
		controller.send(msg, socketAddress);
		
	}
	
	public void displayMessage(String msg) {
		msgPane.getChildren().add(new MessageBox(new MessageBubble(msg, MessageType.RECIEVED)));
	}
	
	

}
