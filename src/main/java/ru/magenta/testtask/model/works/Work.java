package main.java.ru.magenta.testtask.model.works;

import java.time.Duration;

import main.java.ru.magenta.testtask.model.utils.Coordinates;
import main.java.ru.magenta.testtask.model.utils.DistanceCalculator;
import main.java.ru.magenta.testtask.model.utils.TimeWindow;

/**
 * Общий класс родитель для всех типов работ
 * @author Arzakar
 *
 */
public class Work {

	private TimeWindow workingTime;
	private Coordinates startCoords;
	private Coordinates finishCoords;
	
	private double distance;
	
	private Duration pause = Duration.ZERO;
	private Duration delay = Duration.ZERO;
	
	public Work() {
		
	}
	
	public Work(TimeWindow workingTime, Coordinates startCoords, Coordinates finishCoors) {
		this.workingTime = workingTime;
		this.startCoords = startCoords;
		this.finishCoords = finishCoors;
		
		distance = DistanceCalculator.getDistance(startCoords, finishCoords);
	}

	
	
	public TimeWindow getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(TimeWindow workingTime) {
		this.workingTime = workingTime;
	}

	public Coordinates getStartCoords() {
		return startCoords;
	}

	public void setStartCoords(Coordinates startCoords) {
		this.startCoords = startCoords;
	}

	public Coordinates getFinishCoords() {
		return finishCoords;
	}

	public void setFinishCoords(Coordinates finishCoords) {
		this.finishCoords = finishCoords;
	}
	
	public Duration getPause() {
		return pause;
	}
	
	public void setPause(Duration pause) {
		this.pause = pause;
	}
	
	public Duration getDelay() {
		return delay;
	}
	
	public void setDelay(Duration delay) {
		this.delay = delay;
	}
	
	public void calculateDistance() {
		distance = DistanceCalculator.getDistance(startCoords, finishCoords);
	}
	
	public double getDistance() {
		return distance;
	}
	
}
