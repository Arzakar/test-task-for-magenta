package main.java.ru.magenta.testtask.view;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.ru.magenta.testtask.Main;
import main.java.ru.magenta.testtask.model.entities.Car;
import main.java.ru.magenta.testtask.model.entities.DistributionCenter;
import main.java.ru.magenta.testtask.model.utils.Coordinates;
import main.java.ru.magenta.testtask.model.utils.TimeWindow;

public class StartWindowController {

    @FXML
    private TextField latitudeField;

    @FXML
    private TextField longitudeField;

    @FXML
    private TextField speedField;

    @FXML
    private TextField capacityField;

    @FXML
    private TextField countField;

    @FXML
    private TextField startTimeHoursField;

    @FXML
    private TextField startTimeMinutesField;

    @FXML
    private TextField finalTimeHoursField;

    @FXML
    private TextField finalTimeMinutesField;

    @FXML
    private Button openButton;

    @FXML
    private Button closeButton;

	
    
    @FXML
    public void openButtonAction(ActionEvent event) {
    	
    	/* Проверка введённых координат */
    	try {
    		Double.parseDouble(latitudeField.getText());
    		Double.parseDouble(longitudeField.getText());
    	} catch(NumberFormatException e) {
    		showErrorAlert("Некорректный ввод координат",
    					   "В поля можно вводить только символы \"0 - 9\". \nВ качестве точки следует использоваться символ \".\"");	
    		return;
    	}
    	
    	if(Math.abs(Double.parseDouble(latitudeField.getText())) > 90 | Math.abs(Double.parseDouble(longitudeField.getText())) > 180) {
    		showErrorAlert("Некорректный ввод координат",
    					   "Значения широты должны лежать в диапазоне [-90; 90].\nЗначения долготы должны лежать в диапазоне [-180; 180]");
    		return;
    	}
    	
    	/* Проверка введённого времени */
    	try {
    		Integer.parseInt(startTimeHoursField.getText());
    		Integer.parseInt(startTimeMinutesField.getText());
    		Integer.parseInt(finalTimeHoursField.getText());
    		Integer.parseInt(finalTimeMinutesField.getText());
    	} catch(NumberFormatException e) {
    		showErrorAlert("Некорректный ввод времени",
    					   "Значение времени должно содержать только целые числа");
    		return;
    	}
    	
    	LocalTime startTime;
    	LocalTime finalTime;
    	try {
    		startTime = LocalTime.of(Integer.parseInt(startTimeHoursField.getText()), Integer.parseInt(startTimeMinutesField.getText()));
    		finalTime = LocalTime.of(Integer.parseInt(finalTimeHoursField.getText()), Integer.parseInt(finalTimeMinutesField.getText()));
    	} catch(DateTimeException e) {
    		showErrorAlert("Некорректный ввод времени",
					       "Значение часов должно лежать в диапазоне [0: 23]\nЗначение минут должно лежать в диапазоне [0; 59]");
    		return;
    	}
    	
    	if(startTime.isAfter(finalTime) | startTime.equals(finalTime) ) {
    		showErrorAlert("Некорректный ввод времени",
					   	   "Время начала работы должно быть строго меньше времени окончания работы");
    		return;
    	}
    	
    	/* Проверка ввода данных о машине */
    	try {
    		Double.parseDouble(speedField.getText());
    		Double.parseDouble(capacityField.getText());
    		Integer.parseInt(countField.getText());
    	} catch(NumberFormatException e) {
    		showErrorAlert("Некорректный информации о машинах",
					   	   "В поля можно вводить только символы \"0 - 9\". \nВ качестве точки следует использоваться символ \".\""
    					   + "\nЗначение количества машин должно быть целым числом");	
    		return;
    	}
    	
    	if(Double.parseDouble(speedField.getText()) <= 0 | Double.parseDouble(capacityField.getText()) <= 0 | Integer.parseInt(countField.getText()) <= 0) {
    		showErrorAlert("Некорректный информации о машинах",
				   	   	   "Значения в полях ввода должны быть строго больше 0");	
    		return;
    	}
    	
    	
    	try {
    		
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(Main.class.getResource("view/MainWindow.fxml"));
    		AnchorPane mainWindow = (AnchorPane) loader.load();
    		
    		Stage mainWindowStage = new Stage();
    		
    		MainWindowController mainWindowController = loader.getController();
    		
    		mainWindowController.setDc(new DistributionCenter(
    				new Coordinates(Double.parseDouble(latitudeField.getText()), Double.parseDouble(longitudeField.getText())),
    				new TimeWindow(LocalDateTime.of(LocalDate.now().plusDays(1), startTime),
    							   LocalDateTime.of(LocalDate.now().plusDays(1), finalTime)),
    				Integer.parseInt(countField.getText())
    				));
    			
    		mainWindowController.setCar(new Car(
    				Double.parseDouble(speedField.getText()),
    				Double.parseDouble(capacityField.getText())
    				));
    		
    		mainWindowController.setInfoLabel(mainWindowController.getDc().toString() + mainWindowController.getCar().toString());
    		
    		mainWindowStage.setTitle("Окно симулятора");
    		mainWindowStage.setScene(new Scene(mainWindow));
    		mainWindowStage.show();
    		
    		((Stage) openButton.getScene().getWindow()).close();
    		
    	} catch(IOException e) {
    		
    		System.out.println("IOException message: " + e.getMessage());
    		System.out.println("IOException cause: " + e.getCause());

    	}
    }
    
    @FXML
    public void closeButtonAction(ActionEvent event) {
    	
    	((Stage) closeButton.getScene().getWindow()).close();
    	
    }
    
    
    
    private void showErrorAlert(String headerText, String contentText) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Ошибка!");
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
    }
    
}
