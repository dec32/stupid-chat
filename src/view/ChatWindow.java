package view;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import model.message.Message;
import model.message.TextMessage;
import util.MessageType;

public class ChatWindow extends Stage{
	private Controller controller;
	private SocketAddress socketAddress;
	
	private Heart heart = new Heart();
	private VBox heartBar = new VBox();
	private VBox msgPane = new VBox();
	private ScrollPane msgScrollPane;
	private TextField typeField = new TextField();
	private Button sendButton = new Button("发送"); 
	//方便测试，待删除
//	private ConsoleView consoleView = new ConsoleView();
	
	
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
		heartBar.getChildren().add(heart);
		heartBar.setPadding(new Insets(5));
		msgScrollPane = new ScrollPane(msgPane);
		msgScrollPane.setFitToWidth(true);
		msgScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		msgScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		HBox typePane = new HBox(typeField,sendButton);
		mainLayout.getChildren().addAll(heartBar, msgScrollPane, typePane);
		//方便测试，待删除
//		mainLayout.getChildren().add(consoleView);
		
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
		String content = typeField.getText();
		TextMessage textMessage = new TextMessage(socketAddress, content);
		typeField.clear();
		msgPane.getChildren().add(new MessageBox(textMessage, MessageType.SENDED));
		controller.send(textMessage);
		
		
//		String content = typeField.getText();
//		typeField.clear();
//		msgPane.getChildren().add(new MessageBox(new MessageBubble(content, MessageType.SENDED)));
//		controller.send(content, socketAddress);
		
	}
	
	public void displayMessage(Message message) {
		msgPane.getChildren().add(new MessageBox(message, MessageType.RECIEVED));
	}
	
	public void confirmMessage(int id) {
		for(Node n:msgPane.getChildren()) {
			MessageBox mb = (MessageBox)n;
			if(mb.getMessageId() == id) {
				mb.confirm();
			}
		}
	}
	
	public void heartbeat() {
		heart.beat();
	}
	
	

}
