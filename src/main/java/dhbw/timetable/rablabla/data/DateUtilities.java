package dhbw.timetable.rablabla.data;

import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.time.*;
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

    /**
     * Says if the first date is after the second date.
     *
     * @param first The first date
     * @param second The second date
     * @return true if first is after second. Else false.
     */
    public static boolean IsDateOver(LocalDate first, LocalDate second) {
        return first.isAfter(second);
    }

    @Deprecated
    public static boolean IsDateOver(GregorianCalendar first, GregorianCalendar second) {
        return IsDateOver(ConvertToLocalDate(first), ConvertToLocalDate(second));
    }

    /**
     * Changes the day attribute to the last monday.
     * If the current day is already monday, nothing happens.
     *
     * @param src Date to normalize
     * @return Copy of the normalized date
     */
    public static LocalDate Normalize(LocalDate src) {
        return src.minusDays(src.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
    }

    @Deprecated
    public static LocalDate Normalize(GregorianCalendar cal) {
        return Normalize(ConvertToLocalDate(cal));
    }

    /**
     * Changes the day attribute to the next monday.
     * If the current day is already monday, the day will change to monday next week.
     *
     * @param date The date to get the next week from
     * @return Copy of the next week
     */
	public static LocalDate NextWeek(LocalDate date) {
		return Normalize(date.plusDays(7));
	}

	@Deprecated
    public static LocalDate NextWeek(GregorianCalendar date) {
        return NextWeek(ConvertToLocalDate(date));
    }

    /**
     * Produces a string which represents the date of today.
     *
     * @return String in GERMAN_STD_FORMAT
     */
	public static String GetCurrentDate() {
		return GERMAN_STD_FORMAT.format(Calendar.getInstance().getTime());
	}

    /**
     * Checks if two dates are in the same week
     *
     * @param d1 First
     * @param d2 Second
     * @return True if they are in the same week, false if not
     */
	public static boolean IsSameWeek(LocalDate d1, LocalDate d2) {
		WeekFields weekFields = WeekFields.of(Locale.GERMANY);
		return WeekFields.of(Locale.GERMANY) == WeekFields.of(Locale.GERMANY)
				&& d1.getYear() == d2.getYear();
	}

    @Deprecated
    public static boolean IsSameWeek(GregorianCalendar d1, GregorianCalendar d2) {
        return IsSameWeek(ConvertToLocalDate(d1), ConvertToLocalDate(d2));
    }

    /**
     * Clones the day, month and year parameter.
     *
     * @param src The date to clone
     * @return A new local date instance
     */
	public static LocalDate Clone(LocalDate src) {
		return LocalDate.of(src.getYear(), src.getMonth(), src.getDayOfMonth());
	}

    /**
     * Clones the minute, hour, day, month and year parameter.
     *
     * @param src The date to clone
     * @return A new local date instance
     */
	public static LocalDateTime Clone(LocalDateTime src) {
		return LocalDateTime.of(src.getYear(), src.getMonth(), src.getDayOfMonth(), src.getHour(), src.getMinute());
	}

    /**
     * Converts a time range and a day into two LocalDateTime elements.
     *
     * @param date The day of the two times
     * @param times A time range in form 12:00-14:00
     * @return LocalDateTime[] with two elements
     */
	public static LocalDateTime[] ConvertToTime(LocalDate date, String times) {
		String[] timesArray = times.split("-");
		String[] startTime = timesArray[0].split(":");
		String[] endTime = timesArray[1].split(":");
				
		final LocalDateTime start = date.atTime(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));
		final LocalDateTime end = date.atTime(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));

        return new LocalDateTime[]{ start, end };
	}

	/**
	 * Converts java.time.LocalDateTime objects to java.util.Date objects 
	 * using TimeZone Europe/Berlin and Locale.Germany. (ignores seconds)
	 * 
	 * @param date the date to convert
	 * @return new instance of type Date
	 */
	public static Date ConvertToDate(LocalDateTime date) {
        return ConvertToCalendar(date).getTime();
    }

    /**
     * Converts java.time.LocalDate objects to java.util.Date objects
     * using TimeZone Europe/Berlin and Locale.Germany. (ignores seconds)
     *
     * @param date the date to convert
     * @return new instance of type Date
     */
    public static Date ConvertToDate(LocalDate date) {
        return ConvertToCalendar(date).getTime();
    }

    /**
     * Converts java.time.LocalDateTime objects to calendar objects
     * using TimeZone Europe/Berlin and Locale.Germany. (ignores seconds)
     *
     * @param date The date to convert
     * @return an instance of GregorianCalendar
     */
    public static GregorianCalendar ConvertToCalendar(LocalDateTime date) {
        GregorianCalendar tempCal = (GregorianCalendar) Calendar.getInstance(Locale.GERMANY);
        tempCal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        tempCal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), date.getHour(), date.getMinute(), 0);
        return tempCal;
    }

    /**
     * Converts java.time.LocalDate objects to calendar objects
     * using TimeZone Europe/Berlin and Locale.Germany. (ignores seconds)
     *
     * @param date The date to convert
     * @return an instance of GregorianCalendar
     */
    public static GregorianCalendar ConvertToCalendar(LocalDate date) {
	    return ConvertToCalendar(LocalDateTime.of(date, LocalTime.MIN));
    }

    /**
     * Converts calendar objects to java.time.LocalDateTime objects
     * using TimeZone Europe/Berlin and Locale.Germany. (ignores seconds)
     *
     * @param date The date to convert
     * @return an instance of LocalDate
     */
    public static LocalDate ConvertToLocalDate(GregorianCalendar date) {
        return date.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
    }

    /**
     * Converts calendar objects to java.time.LocalDateTime objects
     * using TimeZone Europe/Berlin and Locale.Germany. (ignores seconds)
     *
     * @param date The date to convert
     * @return an instance of LocalDateTime
     */
    public static LocalDateTime ConvertToLocalDateTime(GregorianCalendar date) {
        return date.toInstant().atZone(ZoneId.of("Europe/Berlin")).toLocalDateTime();
    }

    /**
     * Gets latest and earliest times for a week of appointments to set the time borders
     *
     * FIXME Borders are not properly set on a FREE week
     *
     * @param weekAppointments All appointments of this week
     * @return A pair of Integers. First is min, second is max border.
     */
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

    /**
     * Searches the first appointment of a day from within a list of appointments.
     *
     * @param appointments A list of events
     * @param day The day to search the first appointment from
     * @return The appointment if day found, null else
     */
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

    @Deprecated
    public static Appointment GetFirstAppointmentOfDay(ArrayList<Appointment> appointments, GregorianCalendar day) {
        return GetFirstAppointmentOfDay(appointments, ConvertToLocalDate(day));
    }

    /**
     * Filters a list of appointments by its week.
     *
     * @param week The week to match
     * @param superList The list to filter
     * @return The items that matched, an empty list if nothing matched
     */
    public static ArrayList<Appointment> GetWeekAppointmentsOfWeek(LocalDate week, ArrayList<Appointment> superList) {
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

    @Deprecated
    public static ArrayList<Appointment> GetWeekAppointmentsOfWeek(GregorianCalendar week, ArrayList<Appointment> superList) {
        return GetWeekAppointmentsOfWeek(ConvertToLocalDate(week), superList);
    }

    /**
     * Filters a list of appointments to match a day.
     *
     * @param day The day to match
     * @param list The list to search in
     * @return A list of appointments of the given day. The list will be empty
     */
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

    @Deprecated
    public static ArrayList<Appointment> GetAppointmentsOfDay(GregorianCalendar day, ArrayList<Appointment> list) {
        return GetAppointmentsOfDay(ConvertToLocalDate(day), list);
    }

    /**
     * Filters a list of appointments to match a day.
     *
     * @param day The day to match
     * @param list The list to search in
     * @return A set of appointments of the given day. The list will be empty
     */
    public static LinkedHashSet<Appointment> GetAppointmentsOfDay(LocalDate day, LinkedHashSet<Appointment> list) {
        LinkedHashSet<Appointment> dayAppointments = new LinkedHashSet<>();
        String currDate = day.format(GERMAN_STD_FORMATTER);
        for(Appointment a : list) {
            if(a.getDate().equals(currDate)) {
                dayAppointments.add(a);
            }
        }
        return dayAppointments;
    }

    @Deprecated
    public static LinkedHashSet<Appointment> GetAppointmentsOfDay(GregorianCalendar day, LinkedHashSet<Appointment> list) {
        return GetAppointmentsOfDay(ConvertToLocalDate(day), list);
    }

}
