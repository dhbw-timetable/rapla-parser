package dhbw.timetable.rablabla.data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Hendrik Ulbrich (C) 2017
 */
public final class DateUtilities {
	private DateUtilities() {}
	
	public static LocalDate Normalize(LocalDate src) {
	 	return src.minusDays(src.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
	}
	
	public static LocalDate Clone(LocalDate src) {
		return LocalDate.of(src.getYear(), src.getMonth(), src.getDayOfMonth());
	}
	
	public static LocalDateTime Clone(LocalDateTime src) {
		return LocalDateTime.of(src.getYear(), src.getMonth(), src.getDayOfMonth(), src.getHour(), src.getMinute());
	}
	
	public static LocalDateTime[] ConvertToTime(LocalDate date, String times) {
		String[] timesArray = times.split("-");
		String[] startTime = timesArray[0].split(":");
		String[] endTime = timesArray[1].split(":");
				
		final LocalDateTime start = date.atTime(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));
		final LocalDateTime end = date.atTime(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));
		LocalDateTime[] tResult = { start, end };
		
		return tResult;
	}

	/**
	 * Converts java.time.LocalDateTime objects to java.util.Date objects 
	 * using TimeZone Europe/Berlin and Locale.Germany. (ignores seconds)
	 * 
	 * @param src the date to convert
	 * @return new instance
	 */
	public static Date ConvertToDate(LocalDateTime src) {
		Calendar tempCal = Calendar.getInstance(Locale.GERMANY);
		tempCal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		tempCal.set(src.getYear(), src.getMonthValue()-1, src.getDayOfMonth(), src.getHour(), src.getMinute(), 0);
		return tempCal.getTime();
	}
	
}