package dhbw.timetable.rablabla.data;

import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Created by Hendrik Ulbrich (C) 2017
 */
public final class DateUtilities {

    public static final SimpleDateFormat GERMAN_STD_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    public static final DateTimeFormatter GERMAN_STD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.GERMANY);
	private DateUtilities() {}

    public static boolean IsDateOver(LocalDate date, LocalDate isOver) {
        return date.isAfter(isOver);
    }

	public static LocalDate Normalize(LocalDate src) {
	 	return src.minusDays(src.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
	}

	public static void NextWeek(LocalDate d) {
		d = d.plusDays(7);
		Normalize(d);
	}

	public static String GetCurrentDate() {
		return GERMAN_STD_FORMAT.format(Calendar.getInstance().getTime());
	}

	public static boolean IsSameWeek(LocalDate d1, LocalDate d2) {
		WeekFields weekFields = WeekFields.of(Locale.GERMANY);
		return WeekFields.of(Locale.GERMANY) == WeekFields.of(Locale.GERMANY)
				&& d1.getYear() == d2.getYear();
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

        return new LocalDateTime[]{ start, end };
	}

    public static Appointment GetFirstAppointmentOfDay(ArrayList<Appointment> appointments, LocalDate day) {
        for(Appointment a : appointments) {
            // If same day
            if(a.getDate().equals(day.format(GERMAN_STD_FORMATTER))) {
                // Since appointments are partially sorted -> first element is a match
                return a;
            }
        }
        return null;
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
		tempCal.set(src.getYear(), src.getMonthValue() - 1, src.getDayOfMonth(), src.getHour(), src.getMinute(), 0);
		return tempCal.getTime();
	}

    // FIXME Borders are not properly set on a FREE week
    public static Pair<Integer, Integer> GetBorders(ArrayList<Appointment> weekAppointments) {
        int startOnMin, endOnMin, max = 0, min = 1440;

        for (Appointment a : weekAppointments) {
            startOnMin = a.getStartDate().getHour() * 60
                    + a.getStartDate().getMinute();

            endOnMin = a.getEndDate().getHour() * 60
                    + a.getEndDate().getMinute();

            if(startOnMin < min) {
                min = startOnMin;
            }

            if(endOnMin > max) {
                max = endOnMin;
            }
        }
        return new Pair<>(min, max);
    }

    public static ArrayList<Appointment> GetWeekAppointments(LocalDate week, ArrayList<Appointment> superList) {
        ArrayList<Appointment> weekAppointments = new ArrayList<>();
        if (superList != null) {
            for (Appointment a : superList) {
                if (IsSameWeek(a.getStartDate().toLocalDate(), week)) {
                    weekAppointments.add(a);
                }
            }
        }
        return weekAppointments;
    }

    public static ArrayList<Appointment> GetAppointmentsOfDay(LocalDate day, ArrayList<Appointment> list) {
        ArrayList<Appointment> dayAppointments = new ArrayList<>();
        if (list != null) {
            String currDate = day.format(GERMAN_STD_FORMATTER);
            for (Appointment a : list) {
                if (a.getDate().equals(currDate)) {
                    dayAppointments.add(a);
                }
            }
        }
        return dayAppointments;
    }

    public static LinkedHashSet<Appointment> GetAppointmentsOfDayAsSet(LocalDate day, LinkedHashSet<Appointment> list) {
        LinkedHashSet<Appointment> dayAppointments = new LinkedHashSet<>();
        String currDate = day.format(GERMAN_STD_FORMATTER);
        for(Appointment a : list) {
            if(a.getDate().equals(currDate)) {
                dayAppointments.add(a);
            }
        }
        return dayAppointments;
    }
}
