package view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TypeWindow extends Stage{
	private TextField typeField = new TextField();
	private Button confirmButton = new Button("È·¶¨");
	private String title;
	private String typedString;
	public TypeWindow(String title) {
		this.title = title;
		initUI();
		setListener();
	}
	
	private void initUI() {
		this.setTitle(title);
		this.setWidth(280);
		this.setHeight(100);
		this.setResizable(false);
		HBox mainLayout = new HBox(typeField,confirmButton);
		mainLayout.setAlignment(Pos.CENTER);
		mainLayout.setSpacing(20);
		mainLayout.setPadding(new Insets(20));
		this.setScene(new Scene(mainLayout));
	}
	
	private void setListener(){
		confirmButton.setOnAction(e->{
			typedString = typeField.getText();
			this.close();
		});
		typeField.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER) {
				typedString = typeField.getText();
				this.close();
			}
		});
	}

	public String getTypedString() {
		return typedString;
	}
	
}
