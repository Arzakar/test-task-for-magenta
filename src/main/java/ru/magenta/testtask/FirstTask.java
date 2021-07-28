package main.java.ru.magenta.testtask;

import java.time.LocalDateTime;
import java.util.ArrayList;

import main.java.ru.magenta.testtask.model.entities.Car;
import main.java.ru.magenta.testtask.model.entities.DistributionCenter;
import main.java.ru.magenta.testtask.model.entities.Order;
import main.java.ru.magenta.testtask.model.works.Delivery;
import main.java.ru.magenta.testtask.model.works.Packing;
import main.java.ru.magenta.testtask.model.works.Return;
import main.java.ru.magenta.testtask.model.works.Work;

/**
 * Класс солвер для решения первой задачи. <br>
 * В это функции входят:<br>
 * 1. Генерация маршрутов по общему списку маршрутов
 * 2. Генерация расписания работ (из предков класса {@link Work}) для всех маршрутов
 * @author Arzakar
 *
 */
public class FirstTask {

	private DistributionCenter dc;
	private Car car;
	
	private ArrayList<Order> orderList;
	
	private ArrayList<ArrayList<Order>> routeList;

	private ArrayList<ArrayList<Work>> fullWorkList;
	
	
	
	/**
	 * Конструктор, создающий солвер для первой задачи
	 * @param dc - объект распределительного центра {@link DistributionCenter}
	 * @param car - объект машины {@link Car}
	 * @param orderList - список всех заказов
	 */
 	public FirstTask(DistributionCenter dc, Car car, ArrayList<Order> orderList) {
		
		this.dc = dc;
		this.car = car;
		this.orderList = orderList;
		
    	/* Создаём список машин и соответственно всех маршрутов */
    	routeList = new ArrayList<ArrayList<Order>>();
    	for(int i = 0; i < dc.getFleetSize(); i++) {
    		routeList.add(new ArrayList<Order>());
    	}
		
	}
	
	
 	
	/**
	 * Метод заполняющий список маршрутов подсписками, в которых хранятся сами маршруты
	 */
	public void generateRoutes() {
    	
		do {
    		/* Очищение подсписков с маршрутами */
    		for(int i = 0; i < routeList.size(); i++) {
    			routeList.get(i).clear();
    		}
    		
    		/* Распределение заказов по машинам */
    		for(Order order : orderList) {
    			int i =  (int) (Math.random() * dc.getFleetSize());
    			routeList.get(i).add(order);  
    		}
    		
    	} while(routeListIsEmpty(routeList));
    	
		/* Добавление точки распределительного центра в начало и конец маршрутов */
		for(int i = 0; i < routeList.size(); i++) {
			routeList.get(i).add(0, new Order(dc));
			routeList.get(i).add(new Order(dc));
		}
		
		routeList = massCorrectingRouteList(routeList);
		
	}
	
	/* Метод создающий список работ согласно маршруту */
	public ArrayList<Work> generateSchedule(ArrayList<Order> route) {
		
		/* Список работ, которые включает в себя route */
		ArrayList<Work> workList = new ArrayList<>();

		/* Переменная для отслеживания перемещения по заказам в route */
		int iterVar = 0; 
		
		LocalDateTime finalTimePrevWork = dc.getTimeWindow().getStartTime();
		
		/* Цикл выполняется до тех пор, пока в маршруте есть объекты заказов
		 * -1 потому что последний "заказ" в маршруте всегда возвращение в центр распределения */
		while(iterVar < route.size() - 1)
		{
			
			/* Создаёт список подмаршрута, в котором первый "заказ" всегда распределительный центр,
			 * то есть отправление с него, а последующие элементы - нормальные заказы.
			 * Список формируется до тех пор, пока не встретиться "заказ" распределительного центра, что будет означать
			 * возвращение в распределительный центр.
			 * В теле цикла увеличивается переменная iterWar, чтобы на следующей итерации внешнего цикла
			 * программа начала выделение подсписка со следующего "заказа" распределительного центра */
			ArrayList<Order> subRoute = new ArrayList<>();
			subRoute.add(route.get(iterVar));
			iterVar++;
			while(route.get(iterVar).getNumber() != 0) {
				subRoute.add(route.get(iterVar));
				iterVar++;
			}
			
			/* Создаём объект работы по упаковке и присваиваем переменной со значением окончания времени предыдущей работы
			 * время завершения работы по упаковке */
			Packing packing = new Packing(subRoute, finalTimePrevWork, car);
			workList.add(packing);
			finalTimePrevWork = packing.getWorkingTime().getFinalTime();
			
			for(int i = 1; i < subRoute.size(); i++) {
				Delivery delivery = new Delivery(subRoute, i, finalTimePrevWork, car, packing);
				workList.add(delivery);
				finalTimePrevWork = delivery.getWorkingTime().getFinalTime();
			}
			
			/* После выполнения всех работ из подсписка необходимо вернуться на базу 
			 * В конктруктор необходимо передать последнюю работу из списка*/
			Return returnWork = new Return(workList.get(workList.size()-1), dc, car);
			workList.add(returnWork);
			finalTimePrevWork = returnWork.getWorkingTime().getFinalTime();
			
		}
		
		return workList;
	}
	
	/* Метод создающий полное расписание работ на всех маршрутах */
	public void generateFullSchedule() { 
		
		fullWorkList = new ArrayList<ArrayList<Work>>();
		
		for(ArrayList<Order> route : routeList) {
			fullWorkList.add(generateSchedule(route));
		}
	}
	
	
	
    /**
     * Метод проверки наличия пустых маршрутов в списке маршрутов
     * @param routeList - список маршрутов
     * @return - список соодержит / не содержит пустых маршрутов
     */
    private boolean routeListIsEmpty(ArrayList<ArrayList<Order>> routeList) {
    	boolean empty = false;
    	
    	/* Проверка каждого подсписка на отсутствие в нём элементов */
    	for(int i = 0; i < routeList.size(); i++) {
    		if(routeList.get(i).isEmpty()) {
    			empty = true;
    		}
    	}
    	
    	return empty;
    }

    /**
     * Метод корректирующий составленное расписание исходя из максимальной вместимости машины
     * @param routeList - список маршрутов
     * @return - скорректированный список маршрутов
     */
    private ArrayList<ArrayList<Order>> massCorrectingRouteList(ArrayList<ArrayList<Order>> routeList) {
    	
    	ArrayList<ArrayList<Order>> updateRouteList = new ArrayList<ArrayList<Order>>();
    	
    	for(int i = 0; i < routeList.size(); i++) {
    		double cargoMass = 0;
    		
    		updateRouteList.add(new ArrayList<Order>());
    		
    		
    		for(int j = 0; j < routeList.get(i).size(); j++) {
    			
    			cargoMass += routeList.get(i).get(j).getMass();
    			
    			if(cargoMass <= car.getCapacity()) {
    				updateRouteList.get(i).add(routeList.get(i).get(j));
    			} else {
    				updateRouteList.get(i).add(new Order(dc));
    				updateRouteList.get(i).add(routeList.get(i).get(j));
    				cargoMass = routeList.get(i).get(j).getMass();
    			}
    		}
    		
    	}
    	
    	return updateRouteList;
    }
    
    /**
     * Метод создающий строку с маршрутами для отображения в приложении
     * @param routeList - список маршрутов
     * @return - {@link String}
     */
    public String routeListToString() {
    	String route = "";
    	
    	for(int i = 0; i < routeList.size(); i++) {
    		
    		route += "Машина " + (i+1) + ": ";
    		
    		for(int j = 0; j < routeList.get(i).size(); j++) {
    			route += routeList.get(i).get(j).getNumber();
    			if(j != (routeList.get(i).size()-1) ) {
    				route += " -> ";
    			}
    		}
    		
    		route += "\n";
    		
    	}
    	
    	return route;
    }
    
    
    
	public DistributionCenter getDc() {
		return dc;
	}
	
	public void setDc(DistributionCenter dc) {
		this.dc = dc;
	}
	
	public Car getCar() {
		return car;
	}
	
	public void setCar(Car car) {
		this.car = car;
	}

	public ArrayList<Order> getOrderList() {
		return orderList;
	}
	
	public void setOrderList(ArrayList<Order> orderList) {
		this.orderList = orderList;
	}
	
	public ArrayList<ArrayList<Order>> getRouteList() {
		return routeList;
	}
	
	public void setRouteList(ArrayList<ArrayList<Order>> routeList) {
		this.routeList = routeList;
	}
	
	public ArrayList<ArrayList<Work>> getFullWorkList() {
		return fullWorkList;
	}
}
