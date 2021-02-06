package view;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InitWindow extends Stage{
	private Controller controller;
	private Label socketAddressLabel = new Label("我");
	private TextField socketAddressField = new TextField();
	private Label remoteSocketAddressLabel = new Label("对方");
	private TextField remoteSocketAddressField = new TextField();
	private Button startButton = new Button("发起聊天");
	
	public InitWindow(Controller controller,InetSocketAddress publicSocketAddress) {
		this.controller = controller;
		String ip = publicSocketAddress.getAddress().getHostAddress();
		int port = publicSocketAddress.getPort();		
		socketAddressField.setText(ip + ":" + port);
		socketAddressField.setEditable(false);
		VBox mainLayout = new VBox(
			socketAddressLabel,
			socketAddressField,
			remoteSocketAddressLabel,
			remoteSocketAddressField,
			new Separator(),
			startButton
		);
		startButton.prefWidthProperty().bind(mainLayout.widthProperty());
		mainLayout.setPadding(new Insets(10));
		mainLayout.setSpacing(5);
		this.setTitle("初始化");
		this.setWidth(250);
		this.setHeight(180);
		this.setResizable(false);
		this.setScene(new Scene(mainLayout));
		
		startButton.setOnAction(e->{
			on_start();
		});
		remoteSocketAddressField.setOnAction(e->{
			on_start();
		});
		
		this.setOnCloseRequest(e->{
			controller.exit();
		});
	}
	
	private void on_start() {
		String ip = remoteSocketAddressField.getText().split(":")[0];
		int port = Integer.valueOf(remoteSocketAddressField.getText().split(":")[1]);
		try {
			InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getByName(ip), port);
			controller.startChat(inetSocketAddress);
		} catch (UnknownHostException e) {
			System.out.println("无法识别地址或端口号");
		}
		
		
	}
	
}
