package main.java.ru.magenta.testtask.model.utils;

import java.time.Duration;
import java.time.LocalTime;

import main.java.ru.magenta.testtask.model.entities.Order;

/**
 * Вспомогательный метод со статическими методами для расчёта расстояния между точками и
 * времени перемещения между ними
 * @author Arzakar
 *
 */
public class DistanceCalculator {
	
	public static final int  R_EARTH = 6371;
	
	
	/**
	 * Метод определения центрального угла по метрике Great-circle distance
	 * @param coords1 - координаты первой позиции (объект класса {@link Coordinates}
	 * @param coords2 - координаты второй позиции (объект класса {@link Coordinates}
	 * @return - значение центрального угла в рад
	 */
	public static double getCentralAngle(Coordinates coords1, Coordinates coords2) {
		double f1 = Math.toRadians(coords1.getLatitude());
		double l1 = Math.toRadians(coords1.getLongitude());
		double f2 = Math.toRadians(coords2.getLatitude());
		double l2 = Math.toRadians(coords2.getLongitude());
		
		return Math.acos( Math.sin(f1) * Math.sin(f2) +
						  Math.cos(f1) * Math.cos(f2) * Math.cos( Math.abs(l2 - l1) ) );
	}
	
	/**
	 * Метод определения расстояния между двумя точками по метрике Great-circle distance
	 * @param coords1 - координаты первой позиции (объект класса {@link Coordinates}
	 * @param coords2 - координаты второй позиции (объект класса {@link Coordinates}
	 * @return - расстояние между двумя точками в км
	 */
	public static double getDistance(Coordinates coords1, Coordinates coords2) {
		return getCentralAngle(coords1, coords2) * R_EARTH;
	}
	
	/**
	 * Метод определения потребного времени для перемещения между двумя точками
	 * @param coords1 - координаты первой позиции (объект класса {@link Coordinates}
	 * @param coords2 - координаты второй позиции (объект класса {@link Coordinates}
	 * @param speed - скорость объекта
	 * @return значение времени перемещения в виде объекта типа {@link Duration}
	 */
	public static Duration getDurationBetween(Coordinates coords1, Coordinates coords2, double speed) {
		double time = getDistance(coords1, coords2) / speed;
		int minutes = (int) Math.round( ( Math.abs(time) - (int) time ) * 60 );
		
		/* Эта проверка нужна для ситуаций, когда количество минут округляется до 60
		 * Например:
		 * Время получается равным 5.999 ч
		 * Чтобы найти сколько минут составляет дробная часть, нужно 0.999 * 60
		 * Получаем 59.94 минуты, так как по ТЗ результаты нужно округлять до минут,
		 * то я применяю функцию Math.round, а не просто отбрасываю дробную часть.
		 * В результате получаем 60 минут, что является недопустимым значением для LocalTime.
		 * Поэтому я минутам присваиваю значение 0, а к рассчитанным часам прибавляю ещё один
		 */
		if(minutes == 60) {
			time += 1;
			minutes = 0;
		}
		
		LocalTime deltaTime = LocalTime.of((int) time, minutes);
		
		return Duration.between(LocalTime.of(0, 0), deltaTime);
	}
	
	public static Duration getDurationBetween(Order order1, Order order2, double speed) {
		return getDurationBetween(order1.getCoords(), order2.getCoords(), speed);
	}
	
}
