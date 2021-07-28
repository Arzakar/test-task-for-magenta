package main.java.ru.magenta.testtask.model.utils;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Вспомогательный класс для работы с временными окнами
 * @author Arzakar
 *
 */
public class TimeWindow {

	private LocalDateTime startTime;
	private LocalDateTime finalTime;
	private long deltaTimeMinutes;
	private long deltaTimeHours;
	
	
	
	/**
	 * Конструктор класса
	 * @param startTime - время начала временного окна {@link LocalDateTime}
	 * @param finalTime - время окончаня временного окна {@link LocalDateTime}
	 */
	public TimeWindow(LocalDateTime startTime, LocalDateTime finalTime) {
		this.startTime = startTime;
		this.finalTime = finalTime;
		
		deltaTimeMinutes = Duration.between(startTime, finalTime).toMinutes();
		deltaTimeHours = Duration.between(startTime, finalTime).toHours();

	}

	
	
	public String getDeltaTimeString() {
		return String.format("%02d", deltaTimeHours) + ":" + String.format("%02d", (int)(deltaTimeMinutes - deltaTimeHours * 60));
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(LocalDateTime finalTime) {
		this.finalTime = finalTime;
	}
	
	public long getDeltaTimeMinutes() {
		return deltaTimeMinutes;
	}
	
	public long getDeltaTimeHours() {
		return deltaTimeHours;
	}
	
	
	
	@Override
	public String toString() {
		return "Временное окно: Начало   - " + startTime + "\n" + 
			   "                Конец    - " + finalTime + "\n";

	}
}
