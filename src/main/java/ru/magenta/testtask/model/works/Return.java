package main.java.ru.magenta.testtask.model.works;

import java.time.Duration;
import java.time.LocalDateTime;

import main.java.ru.magenta.testtask.model.entities.Car;
import main.java.ru.magenta.testtask.model.entities.DistributionCenter;
import main.java.ru.magenta.testtask.model.utils.Coordinates;
import main.java.ru.magenta.testtask.model.utils.DistanceCalculator;
import main.java.ru.magenta.testtask.model.utils.TimeWindow;

/** 
 * Класс наследник класса {@link Work}. хранящий в себе информацию о работе по возвращению на базу
 * @author Arzakar
 *
 */
public class Return extends Work {

	public Return(TimeWindow workingTime, Coordinates startCoords, Coordinates finishCoors) {
		super(workingTime, startCoords, finishCoors);
	}
	
	public Return(Work lastWork, DistributionCenter dc, Car car) {
		
		super.setStartCoords(lastWork.getFinishCoords());
		super.setFinishCoords(dc.getCoords());
		
		/* Определяем время, за которое машина доберётся с последнего заказа до базы */
		Duration moveToBase = DistanceCalculator.getDurationBetween(getStartCoords(), getFinishCoords(), car.getSpeed());
		
		LocalDateTime startTime = lastWork.getWorkingTime().getFinalTime();
		LocalDateTime finishTime = startTime.plusMinutes(moveToBase.toMinutes());
		
		super.setWorkingTime(new TimeWindow(startTime, finishTime));
	}
	
	
	
	@Override
	public String toString() {
		String string = "";
		string += "Тип: ВОЗВРАЩЕНИЕ НА БАЗУ" + "\n";
		string += "\n";
		string += "Время начала работы      - " + super.getWorkingTime().getStartTime().toLocalTime().toString() + "\n";
		string += "Время окончания работы   - " + super.getWorkingTime().getFinalTime().toLocalTime().toString() + "\n";
		string += "Общее время работы       - " + super.getWorkingTime().getDeltaTimeString() + "\n";
		
		return string;
	}
}
