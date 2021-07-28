package main.java.ru.magenta.testtask.model.entities;

public class Car {

	private double speed;
	private double capacity;
	
	
	/**
	 * Конструктор класса {@link Car}<br>
	 * speed - скорость машины = 0 км/ч
	 * capacity - максимальная вместимость машины = 0 кг
	 */
	public Car() {
		speed = 0;
		capacity = 0;
	}
	
	/**
	 * Конструктор класса {@link Car}
	 * @param speed - скорость машины, км/ч
	 * @param capacity - максимальная вместимость машины, кг
	 */
	public Car(double speed, double capacity) {
		this.speed = speed;
		this.capacity = capacity;
	}
	
	

	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getCapacity() {
		return capacity;
	}
	
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	
	
	
	@Override
	public String toString() {
		return "Машина:" + "\n" +
			   "Скорость - " + speed + " км/с" + "\n" +
			   "Вместимость - " + capacity + " кг" + "\n";
	}
	
}

