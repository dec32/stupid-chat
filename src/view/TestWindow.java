package view;

import java.io.IOException;
import java.io.OutputStream;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Model;

public class TestWindow extends Stage{
	private Model model;
	private TextArea msgArea = new TextArea();
	private TextArea typeArea = new TextArea();
	private String remoteHost;
	private int remotePort;
	
	public TestWindow(Model model) {
		this.model = model;
		VBox mainLayout = new VBox(msgArea, typeArea);
		typeArea.setPrefHeight(70);
		VBox.setVgrow(msgArea, Priority.ALWAYS);
		mainLayout.setSpacing(10);
		mainLayout.setPadding(new Insets(10));		
		typeArea.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER) {
				String msg = typeArea.getText();
				typeArea.clear();
				model.send(msg, remoteHost, remotePort);
			}
		});
		redirectSystemOut();
		this.setWidth(400);
		this.setHeight(400);
		this.setTitle("Stupid Chat");
		this.setScene(new Scene(mainLayout));
		this.setOnCloseRequest(e->{
			model.exit();
		});
	}
	
	
	public void launch() {
		model.launch();
		TypeWindow typeWindow = new TypeWindow("输入对方地址和端口");
		typeWindow.showAndWait();
		remoteHost = typeWindow.getTypedString().split(":")[0];
		remotePort = Integer.valueOf(typeWindow.getTypedString().split(":")[1]);
		System.out.println("对方地址和端口：" + remoteHost + ":" + remotePort);
	}
	
	private void redirectSystemOut() {
		/*
		 * 这里要提供一个没有任何用处的OutputStream
		 * 原因请看 TextAreaPrintStream 类的解释
		 * 我知道这样写实在是太丑了, 但是我太蠢了所以想不到别的办法
		 */
		OutputStream ops = new OutputStream() {		
			@Override
			public void write(int b) throws IOException {
			}
		};	
		System.setOut(new TextAreaPrintStream(ops, msgArea));
	}
}
