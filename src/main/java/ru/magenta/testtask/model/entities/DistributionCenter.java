package main.java.ru.magenta.testtask.model.entities;

import main.java.ru.magenta.testtask.model.utils.Coordinates;
import main.java.ru.magenta.testtask.model.utils.TimeWindow;

public class DistributionCenter {

	private Coordinates coords;
	private TimeWindow timeWindow;
	private int fleetSize;
	
	
	/**
	 * Конструктор класса {@link DistributionCenter}
	 * @param coord - координаты распределительного центра (объект класса {@link Coordinates})
	 * @param timeWindow - время работы распределительного центра (объект класса {@link TimeWindow})
	 * @param fleetSize - колиечство машин, доступных распределительному центру
	 */
	public DistributionCenter(Coordinates coord, TimeWindow timeWindow, int fleetSize) {
		this.coords = coord;
		this.timeWindow = timeWindow;
		this.fleetSize = fleetSize;
	}



	public Coordinates getCoords() {
		return coords;
	}

	public void setCoords(Coordinates coords) {
		this.coords = coords;
	}

	public TimeWindow getTimeWindow() {
		return timeWindow;
	}

	public void setTimeWindow(TimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	public int getFleetSize() {
		return fleetSize;
	}

	public void setFleetSize(int fleetSize) {
		this.fleetSize = fleetSize;
	}
	
	
	
	@Override
	public String toString() {
		return "Распределительный центр: " + "\n" +
			   coords.toString() + 
			   timeWindow.toString() +
			   "Количество машин         - " + fleetSize + "\n";
	}
	
}
