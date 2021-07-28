package main.java.ru.magenta.testtask.model.works;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import main.java.ru.magenta.testtask.model.entities.Car;
import main.java.ru.magenta.testtask.model.entities.Order;
import main.java.ru.magenta.testtask.model.utils.Coordinates;
import main.java.ru.magenta.testtask.model.utils.DistanceCalculator;
import main.java.ru.magenta.testtask.model.utils.TimeWindow;

/**
 * Класс наследник класса {@link Work}, хранящий в себе информацию о работе по упаковке заказов
 * @author Arzakar
 *
 */
public class Packing extends Work {

	private ArrayList<Order> orderList;
	
	public Packing() {
		
	}
	
	public Packing(TimeWindow workingTime, Coordinates startCoords, Coordinates finishCoors) {
		super(workingTime, startCoords, finishCoors);
	}
	
	/**
	 * Конструктор, создающий работу по погрузке, исходя из подсписка маршрута, где начальный элемент всегда центр распределения
	 * а последующие - заказы, вплоть до следующего центра распределния
	 * @param orderList - подсписок общего маршрута, где первый элемент всегда центр распределения
	 * @param prevTime - время окончания предыдущей работы
	 * @param car - машина осуществляющая данную работу
	 */
	public Packing(ArrayList<Order> orderList, LocalDateTime prevTime, Car car) {
		
		this.orderList = orderList;
		
		/* Определяем время, за которое происходит упаковка и загрузка всех заказов в машину */
		Duration packingDuration = Duration.ZERO;
		
		for(Order order : orderList) {
			packingDuration = packingDuration.plusHours(order.getTimePack().getHour())
									 		 .plusMinutes(order.getTimePack().getMinute());
		}
		
		/* Определяем время, необходимое, чтобы добраться до первого заказа в списке (после центра распределения) */
		Duration moveToOrderDuration = DistanceCalculator.getDurationBetween(orderList.get(0), orderList.get(1), car.getSpeed());
		
		/* Вычисляем общее время, которое нужно, чтобы упаковать заказы и доехать до первого */
		Duration sumDuration = packingDuration.plus(moveToOrderDuration);
		
		/* Исходя из расчётов, находим минимальное время, к которому можно привезти первый в списке заказ,
		 * после выполнения предыдущей работы */
		LocalDateTime packAndMoveTime = prevTime.plusMinutes(sumDuration.toMinutes());
		
		LocalDateTime startTime;
		LocalDateTime finishTime;
		
		/* 
		 * Проверяем:
		 * если минимальное время меньше, чем время, когда открывается окно доступности первого заказа,
		 * то мы определяем самое позднее время, в которое необходимо начать погрузку, чтобы успеть аккурат к моменту открытия окна доступности,
		 * иначе мы начинаем погрузку сразу же после выполнения предыдущего заказа
		 */
		if(packAndMoveTime.isBefore(orderList.get(1).getTimeWindow().getStartTime())) {
			
			startTime = orderList.get(1).getTimeWindow()
													  .getStartTime()
													  .minusMinutes(sumDuration.toMinutes());
			
			finishTime = startTime.plusMinutes(packingDuration.toMinutes());
			
		} else {
			
			startTime = prevTime;
			finishTime = startTime.plusMinutes(packingDuration.toMinutes());
			
		}
		
		super.setWorkingTime(new TimeWindow(startTime, finishTime));
	}
	
	
	
	@Override
	public String toString() {
		String string = "";
		string = "Тип: ПОГРУЗКА" + "\n\n";
		
		string += "Заказы                   - ";
		for(int i = 1; i < orderList.size(); i++) {
			string += orderList.get(i).getNumber() + " ";
		}
		string += "\n";
		
		double mass = 0;
		for(int i = 1; i < orderList.size(); i++) {
			mass += orderList.get(i).getMass();
		}
	
		string += "Общая масса              - " + mass + " кг" + "\n";

		string += "Время начала погрузки    - " + super.getWorkingTime().getStartTime().toLocalTime() + "\n";
		string += "Время окончания погрузки - " + super.getWorkingTime().getFinalTime().toLocalTime();
		
		string += "\n";
		
		return string;
	}
}
