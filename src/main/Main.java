package main;

import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.ChatWindow;
import view.InitWindow;
import view.View;

public class Main extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
//		new ChatWindow().show();
//		new InitWindow().show();
		
		
		Model model = new Model();
		View view = new View();
		Controller controller = new Controller();
		
		model.setView(view);
		
		view.setController(controller);
		view.setModel(model);
		
		controller.setModel(model);
		controller.setView(view);
		
		model.launch();
	}
	
}
