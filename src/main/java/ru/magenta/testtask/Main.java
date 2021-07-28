package main.java.ru.magenta.testtask;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(Main.class.getResource("view/StartWindow.fxml"));
    		AnchorPane mainWindow = (AnchorPane) loader.load();

    		primaryStage.setTitle("Окно ввода начальных данных");
			primaryStage.setScene(new Scene(mainWindow));
			primaryStage.show();
		
			
		} catch(IOException e) {
			
			System.out.println("IOException message: " + e.getMessage());
    		System.out.println("IOException cause: " + e.getCause());
    		
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
