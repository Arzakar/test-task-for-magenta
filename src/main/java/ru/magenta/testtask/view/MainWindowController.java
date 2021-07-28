package main.java.ru.magenta.testtask.view;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import main.java.ru.magenta.testtask.FirstTask;
import main.java.ru.magenta.testtask.model.entities.Car;
import main.java.ru.magenta.testtask.model.entities.DistributionCenter;
import main.java.ru.magenta.testtask.model.entities.Order;
import main.java.ru.magenta.testtask.model.utils.Coordinates;
import main.java.ru.magenta.testtask.model.utils.OrderGenerator;
import main.java.ru.magenta.testtask.model.utils.TimeWindow;
import main.java.ru.magenta.testtask.model.works.Work;

public class MainWindowController {
	
	/* FXML поля */
	@FXML
    private Label infoLabel;

	public void setInfoLabel(String string) {
		infoLabel.setText(string);
	}
	
    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, Integer> numberColumn;
    
    @FXML
    private TableColumn<Order, Coordinates> coordinatesColumn;
    
    @FXML
    private TableColumn<Order, Double> latitudeColumn;

    @FXML
    private TableColumn<Order, Double> longitudeColumn;

    @FXML
    private TableColumn<Order, TimeWindow> timeWindowColumn;
    
    @FXML
    private TableColumn<Order, LocalDateTime> startTimeColumn;

    @FXML
    private TableColumn<Order, LocalDateTime> finalTimeColumn;

    @FXML
    private TableColumn<Order, Double> massColumn;

    @FXML
    private TableColumn<Order, LocalTime> packTimeColumn;

    @FXML
    private TableColumn<Order, LocalTime> unpackTimeColumn;

    @FXML
    private TextField orderCountField;

    @FXML
    private Button generateButton;

    @FXML
    private Canvas mapCanvas;
    
    @FXML
    private Button genereateRouteButton;
    
    @FXML
    private TextArea routeTextArea;
    
    @FXML
    private Button createScheduleButton;
    
    @FXML
    private TextArea scheduleTextArea;
    
    @FXML
    private ComboBox<String> carNumberComboBox;
    
    /* Прочие поля */
    private Car car;
    private DistributionCenter dc;
    
    /* Список заказов */
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    
    /* Поля заданий */
    FirstTask firstTask;
    
    
    
    @FXML
   	private void initialize() {
    	
    	numberColumn.setCellValueFactory(new Callback<CellDataFeatures<Order, Integer>, ObservableValue<Integer>>() {
    		public ObservableValue<Integer> call(CellDataFeatures<Order, Integer> order) {
    			return new ReadOnlyObjectWrapper<Integer>(order.getValue().getNumber());
    	    }
    	});

    	latitudeColumn.setCellValueFactory(new Callback<CellDataFeatures<Order, Double>, ObservableValue<Double>>() {
    		public ObservableValue<Double> call(CellDataFeatures<Order, Double> order) {
    			return new ReadOnlyObjectWrapper<Double>(order.getValue().getCoords().getLatitude());
    	    }
    	});
    	
    	longitudeColumn.setCellValueFactory(new Callback<CellDataFeatures<Order, Double>, ObservableValue<Double>>() {
   	     	public ObservableValue<Double> call(CellDataFeatures<Order, Double> order) {
   	     		return new ReadOnlyObjectWrapper<Double>(order.getValue().getCoords().getLongitude());
   	     	}
    	});
    	
    	startTimeColumn.setCellValueFactory(new Callback<CellDataFeatures<Order, LocalDateTime>, ObservableValue<LocalDateTime>>() {
    		public ObservableValue<LocalDateTime> call(CellDataFeatures<Order, LocalDateTime> order) {
   	     		return new ReadOnlyObjectWrapper<LocalDateTime>(order.getValue().getTimeWindow().getStartTime());
   	     	}
    	});
    	
    	finalTimeColumn.setCellValueFactory(new Callback<CellDataFeatures<Order, LocalDateTime>, ObservableValue<LocalDateTime>>() {
    		public ObservableValue<LocalDateTime> call(CellDataFeatures<Order, LocalDateTime> order) {
   	     		return new ReadOnlyObjectWrapper<LocalDateTime>(order.getValue().getTimeWindow().getFinalTime());
   	     	}
    	});
    	
    	massColumn.setCellValueFactory(new Callback<CellDataFeatures<Order, Double>, ObservableValue<Double>>() {
    		public ObservableValue<Double> call(CellDataFeatures<Order, Double> order) {
    			return new ReadOnlyObjectWrapper<Double>(order.getValue().getMass());
    	    }
    	});
    	
    	packTimeColumn.setCellValueFactory(new Callback<CellDataFeatures<Order, LocalTime>, ObservableValue<LocalTime>>() {
    		public ObservableValue<LocalTime> call(CellDataFeatures<Order, LocalTime> order) {
   	     		return new ReadOnlyObjectWrapper<LocalTime>(order.getValue().getTimePack());
   	     	}
    	});
    	
    	unpackTimeColumn.setCellValueFactory(new Callback<CellDataFeatures<Order, LocalTime>, ObservableValue<LocalTime>>() {
    		public ObservableValue<LocalTime> call(CellDataFeatures<Order, LocalTime> order) {
   	     		return new ReadOnlyObjectWrapper<LocalTime>(order.getValue().getTimeUnpack());
   	     	}
    	});
    	
    }
    
    
    
    public void generateButtonAction(ActionEvent event) {
    	
    	/* Проверка правильности ввода необходимого количества заказов */
    	try {
    		Integer.parseInt(orderCountField.getText());
    	} catch(NumberFormatException e) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Ошибка!");
    		alert.setHeaderText("Неверный ввод необходимого количества заказов");
    		alert.setContentText("Значение в поле ввода должно быть целым числом");
    		alert.showAndWait();
    		return;
    	}
    	
    	if(Integer.parseInt(orderCountField.getText()) <= 0 | Integer.parseInt(orderCountField.getText()) < dc.getFleetSize()) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Ошибка!");
    		alert.setHeaderText("Неверный ввод необходимого количества заказов");
    		alert.setContentText("Значение количества заказов должно быть строго больше 0.\nКоличество заказов должно быть больше, чем количество машин.");
    		alert.showAndWait();
    		return;
    	}
    	
    	/* Формирование списка заказов */
    	if(!orderList.isEmpty()) {
    		orderList.clear();
    		orderTable.getItems().clear();
    	}
    	
    	ArrayList<Order> orders = new ArrayList<>();
    	
    	OrderGenerator og = new OrderGenerator(car, dc);
    	
    	/* Присваиваем каждому заказу уникальный номер начиная с 1, потому что номер заказа 0 означает,
    	 * что заказ ассоциирован с возвращением на базу */
    	for(int i = 0; i < Integer.parseInt(orderCountField.getText()); i++) {
    		Order order = new Order(og.generate());
    		order.setNumber(i+1);
    		orders.add(order);
    	}
    	
    	orderList = FXCollections.observableArrayList(orders);

    	orderTable.getItems().addAll(orderList);
    	
    	routeTextArea.clear();
    	scheduleTextArea.clear();
    	
    	genereateRouteButton.setDisable(false);
    	createScheduleButton.setDisable(true);
    	
    }
    
    public void generateRouteButtonAction(ActionEvent event) {

		firstTask = new FirstTask(dc, car, new ArrayList<Order>(orderTable.getItems()));

    	firstTask.generateRoutes();
    	firstTask.generateFullSchedule();

    	routeTextArea.setText(firstTask.routeListToString());
    	
    	for(int i = 0; i < firstTask.getRouteList().size(); i++) {
    		carNumberComboBox.getItems().add("Машина №" + (i+1));
    	}
    	
    	carNumberComboBox.setDisable(false);
    	createScheduleButton.setDisable(false);
    	
    	scheduleTextArea.clear();
    }
    
    public void createScheduleButtonAction(ActionEvent event) {
    	
    	scheduleTextArea.clear();
    	
    	ArrayList<Work> workList = firstTask.getFullWorkList().get(carNumberComboBox.getSelectionModel().getSelectedIndex());
    	
    	String string = "";
    	
    	string += "Общая информация" + "\n";
    	string += "\n";
    	string += "Время начала маршрута    - " + workList.get(0).getWorkingTime().getStartTime() + "\n";
    	string += "Время окончания маршрута - " + workList.get(workList.size() - 1).getWorkingTime().getFinalTime() + "\n";
    	string += "\n";
    	
    	Duration sumPause = Duration.ZERO;
    	Duration sumDelay = Duration.ZERO;
    	
    	for(Work works : workList) {
    		sumPause = sumPause.plus(works.getPause());
    		sumDelay = sumDelay.plus(works.getDelay());
    	}
    	
    	if(sumPause.isZero()) {
    		string += "Суммарные простои        - НЕТ" + "\n";
    	} else {
    		string += "Суммарные простои        - " + sumPause.toString() + "\n";
    	}
    	
    	if(sumDelay.isZero()) {
    		string += "Суммарные задержки       - НЕТ" + "\n";
    		string += "Расписание               - ВАЛИДНО" + "\n";
    	} else {
    		string += "Суммарные задержки       - " + sumDelay.toString() + "\n";
    		string += "Расписание               - НЕ ВАЛИДНО" + "\n";
    	}
    	
    	string += "\n";
    	
    	scheduleTextArea.appendText(string);
    	
    	for(Work works : workList) {
    		scheduleTextArea.appendText("---------------------------" + "\n\n");
    		scheduleTextArea.appendText(works.toString() + "\n");
    	}
    	
    	scheduleTextArea.positionCaret(0);
    }

    
    
    public void setCar(Car car) {
    	this.car = car;
    }

    public Car getCar() {
    	return car;
    }
    
	public void setDc(DistributionCenter dc) {
		this.dc = dc;
	}
    
	public DistributionCenter getDc() {
		return dc;
	}
	
}
