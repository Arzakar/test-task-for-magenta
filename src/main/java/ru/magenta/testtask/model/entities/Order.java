package main.java.ru.magenta.testtask.model.entities;

import java.time.LocalTime;

import main.java.ru.magenta.testtask.model.utils.Coordinates;
import main.java.ru.magenta.testtask.model.utils.TimeWindow;

public class Order {

	private int number;
	private Coordinates coords;
	private double mass;
	private TimeWindow timeWindow;
	private LocalTime timePack;
	private LocalTime timeUnpack;
	
	
	
	public Order(Order order) {
		coords = order.getCoords();
		mass = order.getMass();
		timeWindow = order.getTimeWindow();
		timePack = order.getTimePack();
		timeUnpack = order.getTimeUnpack();
	}
	
	/**
	 * Конструктор класса {@link Order}
	 * @param coords - координаты распределительного центра (объект класса {@link Coordinates})
	 * @param mass - масса заказа
	 * @param timeWindow - время работы распределительного центра (объект класса {@link TimeWindow})
	 * @param timePack - время на упаковку заказа
	 * @param timeUnpack - время на распаковку заказа
	 */
	public Order(Coordinates coords, double mass, TimeWindow timeWindow, LocalTime timePack, LocalTime timeUnpack) {
		this.coords = coords;
		this.mass = mass;
		this.timeWindow = timeWindow;
		this.timePack = timePack;
		this.timeUnpack = timeUnpack;
	}

	/**
	 * Конструктор для заказа, обозначающего базу
	 * @param dc - объект центра распределения {@link DistributionCenter}
	 */
	public Order(DistributionCenter dc) {
		number = 0;
		coords = dc.getCoords();
		mass = 0;
		timePack = LocalTime.of(0, 0);
		timeUnpack = LocalTime.of(0, 0);
	}
	
	
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Coordinates getCoords() {
		return coords;
	}

	public void setCoords(Coordinates coords) {
		this.coords = coords;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public TimeWindow getTimeWindow() {
		return timeWindow;
	}

	public void setTimeWindow(TimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	public LocalTime getTimePack() {
		return timePack;
	}

	public void setTimePack(LocalTime timePack) {
		this.timePack = timePack;
	}

	public LocalTime getTimeUnpack() {
		return timeUnpack;
	}
	
	public void setTimeUnpack(LocalTime timeUnpack) {
		this.timeUnpack = timeUnpack;
	}
	
	
	
	@Override
	public String toString() {
		return "Заказ №" + number + "\n" +
			   coords.toString() +
			   timeWindow.toString() + 
			   "Масса                    - " + mass + " кг" + "\n" +
			   "Время упаковки заказа    - " + timePack.getMinute() + " мин" + "\n" +
			   "Время распаковки заказа  - " + timeUnpack.getMinute() + " мин" + "\n";
	}
}
