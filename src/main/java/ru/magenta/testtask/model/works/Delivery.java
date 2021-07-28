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
 * Класс наследник класса {@link Work}, который хранит в себе информацию о работе по доставке заказа
 * @author Arzakar
 *
 */
public class Delivery extends Work {

	private Order order;
	private Packing packing;
	
	
	
	public Delivery(TimeWindow workingTime, Coordinates startCoords, Coordinates finishCoors) {
		super(workingTime, startCoords, finishCoors);
	}

	
	
	/**
	 * Конструктор, создающий работу по погрузке, исходя из подсписка маршрута, где начальный элемент 
	 * всегда центр распределения, а последующие - заказы, вплоть до следующего центра распределния. 
	 * Так же необходимо передать параметр номера заказа в списке
	 * @param orderList - подсписок общего маршрута с элементами {@link Order}, где первый элемент всегда центр распределения
	 * @param numberOrder - номер заказа из подсписка, который будет обслуживаться в данной работе (не может быть равен 0, 
	 * так как на этой позиции
	 * всегда находится распределительный центр)
	 * @param prevTime - время окончания предыдущей работы {@link LocalDateTime}
	 * @param car - машина осуществляющая данную работу {@link Car}
	 */
	public Delivery(ArrayList<Order> orderList, int numberOrder, LocalDateTime prevTime, Car car, Packing packing) {

		super.setStartCoords(orderList.get(numberOrder - 1).getCoords());
		super.setFinishCoords(orderList.get(numberOrder).getCoords());
		super.calculateDistance();
		this.packing = packing;
		
		order = orderList.get(numberOrder);
		
		/* Определяем время, за которое машина доставит заказ из предыдущей точки в маршруте */
		Duration moveToOrderDuration = DistanceCalculator.getDurationBetween(super.getStartCoords(), super.getFinishCoords(), car.getSpeed());
		
		/* Определяем время, которое необходимо на распаковку заказа */
		Duration unpackDuration = Duration.ZERO;
		unpackDuration = unpackDuration.plusHours(orderList.get(numberOrder).getTimeUnpack().getHour())
									   .plusMinutes(orderList.get(numberOrder).getTimeUnpack().getMinute());
		
		/* Вычисляем общее время выполнения работы */
		Duration sumDuration = moveToOrderDuration.plus(unpackDuration);
		
		LocalDateTime moveAndUnpackTime = prevTime.plusMinutes(sumDuration.toMinutes());
		
		LocalDateTime startTime;
		LocalDateTime finishTime;
		
		/* Проверяем
		 * если время к которому машина приедет на заказ меньше, чем начало окна доступности,
		 * 	то машине придётся подождать на месте предыдущей работы и время начала текущей работы сместиться
		 * иначе, если время к которому машина приедет на заказ и распакует его, меньше конца окна доступности,
		 * 	то время начала текущей работы есть время окончания предыдущей и операция выполнится вовремя
		 * иначе заказ выполнен с опозданием
		 */
		if(prevTime.plusMinutes(moveToOrderDuration.toMinutes()).isBefore(orderList.get(numberOrder).getTimeWindow().getStartTime())) {
			
			startTime = orderList.get(numberOrder).getTimeWindow()
																.getStartTime()
																.minusMinutes(moveToOrderDuration.toMinutes());
			
			finishTime = startTime.plusMinutes(sumDuration.toMinutes());
			
			super.setPause(Duration.between(prevTime, startTime));
			
		} else if (moveAndUnpackTime.isBefore(orderList.get(numberOrder).getTimeWindow().getFinalTime())) {
			
			startTime = prevTime;
			finishTime = startTime.plusMinutes(sumDuration.toMinutes());
			
		} else {
			
			startTime = prevTime;
			finishTime = startTime.plusMinutes(sumDuration.toMinutes());
			
			super.setDelay(Duration.between(orderList.get(numberOrder).getTimeWindow().getFinalTime(), finishTime));
			
		}
		
		super.setWorkingTime(new TimeWindow(startTime, finishTime));
		
	}
	
	
	
	@Override
	public String toString() {
		String string = "";
		string += "Тип: ДОСТАВКА" + "\n";
		string += "\n";
		string += order.toString();
		string += "\n";
		string += "Пройденное расстояние    - " + String.format("%.3f", super.getDistance())  + " км" + "\n";
		string += "Время начала работы      - " + super.getWorkingTime().getStartTime().toLocalTime().toString() + "\n";
		string += "Время окончания работы   - " + super.getWorkingTime().getFinalTime().toLocalTime().toString() + "\n";
		string += "Общее время работы       - " + super.getWorkingTime().getDeltaTimeString() + "\n";
		
		TimeWindow tw = new TimeWindow(packing.getWorkingTime().getStartTime(), super.getWorkingTime().getFinalTime());
		string += "\n";
		string += "Время обслуживания заказа - " + tw.getDeltaTimeString() + "\n";
		string += "(с момента упаковки заказа на базе)" + "\n";
		
		string += "\n";
		if(super.getDelay().isZero()) {
			string += "Заказ доставлен в срок";
		} else { 
			string += "Заказ доставлен с задержкой в " + super.getDelay().toHours() + "ч " + 
														 (int) (super.getDelay().toMinutes() - super.getDelay().toHours() * 60) + "мин";
		}
		string += "\n";
		return string;
	}
	
}
