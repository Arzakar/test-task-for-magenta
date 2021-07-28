package main.java.ru.magenta.testtask.model.utils;

/**
 * Вспомогательный класс, содержаний специальные математические функции
 * @author Arzakar
 *
 */
public class MathOperations {

	/**
	 * Метод возвращаюший случайное значение из заданного диапазона
	 * @param min - минимальное значение 
	 * @param max - максимальное значение
	 * @return - случайная величина
	 */
	public static double randomFromRange(double min, double max) {
		max -= min;
		return ( Math.random() * ++max ) + min;
	}
	
}
