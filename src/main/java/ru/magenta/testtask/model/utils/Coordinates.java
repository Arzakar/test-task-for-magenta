package main.java.ru.magenta.testtask.model.utils;

/**
 * Вспомогательный класс для работы с координатами
 * @author Arzakar
 *
 */
public class Coordinates {

	private double latitude;
	private double longitude;
	
	
	
	public Coordinates() {
		latitude = 0;
		longitude = 0;
	}
	
	/**
	 * Конструктор класса {@link Coordinates}
	 * @param latitude - значение широты, град
	 * @param longitude - значение долготы, град
	 */
	public Coordinates(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	
	

	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	
	@Override
	public String toString() {
		return "Координаты:     Широта   - " + String.format("%.6f", latitude) + " град" + "\n" +
			   "                Долгота  - " + String.format("%.6f", longitude) + " град" + "\n";
	}
}
