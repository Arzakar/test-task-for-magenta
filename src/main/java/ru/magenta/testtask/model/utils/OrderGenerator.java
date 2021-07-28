package main.java.ru.magenta.testtask.model.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import main.java.ru.magenta.testtask.model.entities.Car;
import main.java.ru.magenta.testtask.model.entities.DistributionCenter;
import main.java.ru.magenta.testtask.model.entities.Order;

/**
 * Класс, создающий {@link Order} со случайными значениями
 * @author Arzakar
 *
 */
public class OrderGenerator {

	private Car car;
	private DistributionCenter dc;
	
	private double maxCentralAngle;	
	private double maxDistance;	

	
	/**
	 * Конструктор класса {@OrderGenerator}
	 * @param car - объект класса {@link Car}
	 * @param dc - объект класса {@link DistributionCenter}
	 */
	public OrderGenerator(Car car, DistributionCenter dc) {
		this.car = car;
		this.dc = dc;

		maxDistance = ( (double) dc.getTimeWindow().getDeltaTimeMinutes() / ( 2 * 60 ) ) * car.getSpeed();
		maxCentralAngle = maxDistance / DistanceCalculator.R_EARTH;
	}

	
	/**
	 * Метод генерирующий заказ
	 * @return - {@link Order} со случайными характеристиками
	 */
	public Order generate() {

		Coordinates orderCoords = new Coordinates();
		double centralAngle;

		
		
		// Генерация координат заказа
		do {
			
			double minLatitude = dc.getCoords().getLatitude() - Math.toDegrees(maxCentralAngle);
			double maxLatitude = dc.getCoords().getLatitude() + Math.toDegrees(maxCentralAngle);
			double minLongitude = dc.getCoords().getLongitude() - Math.toDegrees(maxCentralAngle);
			double maxLongitude = dc.getCoords().getLongitude() + Math.toDegrees(maxCentralAngle);
			
			double randomLatitude = MathOperations.randomFromRange(minLatitude, maxLatitude);;
			double randomLongitude = MathOperations.randomFromRange(minLongitude, maxLongitude);;
			
			orderCoords.setLatitude(randomLatitude);
			orderCoords.setLongitude(randomLongitude);
			
			centralAngle = DistanceCalculator.getCentralAngle(dc.getCoords(), orderCoords);
			
		} while (centralAngle > maxCentralAngle);
		
		
		
		// Генерация массы заказа
		double massOrder = (int) MathOperations.randomFromRange(2, car.getCapacity());
		
		
		
		// Генерация временного окна заказа
		LocalDateTime startOrderTime = LocalDateTime.of(LocalDate.now().plusDays(1),
														LocalTime.of((int) MathOperations.randomFromRange(dc.getTimeWindow().getStartTime().getHour() + 1, 
																										  dc.getTimeWindow().getFinalTime().getHour() - 1), 0));
		LocalDateTime finalOrderTime = LocalDateTime.of(LocalDate.now().plusDays(1),
														LocalTime.of((int) MathOperations.randomFromRange(startOrderTime.getHour() + 1, 
																										  dc.getTimeWindow().getFinalTime().getHour()), 0));
		
		TimeWindow orderTimeWindow = new TimeWindow(startOrderTime, finalOrderTime);
		
		
		
		// генерация времени сбора и выдачи заказа
		LocalTime packOrderTime = LocalTime.of(0, (int) MathOperations.randomFromRange(5, 59));
		LocalTime unpackOrderTime = LocalTime.of(0, (int) MathOperations.randomFromRange(5, 59));
		
		
		
		return new Order(orderCoords, massOrder, orderTimeWindow, packOrderTime, unpackOrderTime);
	}
	
	
	
	public double getMaxCentralAngle() {
		return maxCentralAngle;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	

	@Override
	public String toString() {
		return "Параметры генератора заказов" + "\n" +
			   "---" + "\n" +
			   car.toString() +
			   "---" + "\n" +
			   dc.toString() +
			   "---" + "\n" +
			   "Максимальный радиус обслуживания - " + maxDistance + "\n" + 
			   "Центральный угол максимального радиуса обслуживания - " + Math.toDegrees(maxCentralAngle) + " град"+ "\n";
	}
	
}
