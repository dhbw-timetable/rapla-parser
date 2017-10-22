package dhbw.timetable.rablabla.data;

import java.time.LocalDateTime;

/**
 * An deep-immutable struct concept based class for storing the appointment data of the lectures.
 * 
 * Created by Hendrik Ulbrich (C) 2017
 */
public final class Appointment extends Lecture {

	private LocalDateTime start, end;

    public Appointment(LocalDateTime start, LocalDateTime end, String course, String info) {
        super(course, info);
        this.start = start;
        this.end = end;
    }

	public String getStartTime() {
		return start.format(DateUtilities.GERMAN_STD_TIMEFORMATTER());
	}

	public String getEndTime() {
		return end.format(DateUtilities.GERMAN_STD_TIMEFORMATTER());
	}

	public String getDate() {
		return end.format(DateUtilities.GERMAN_STD_DATEFORMATTER());
	}

	public LocalDateTime getStartDate() {
		return start;
	}

	public LocalDateTime getEndDate() {
		return end;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof Appointment) {
				Appointment that = (Appointment) o;
				return that.toString().equals(this.toString());
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(getDate())
				.append("\t").append(getStartTime()).append("-").append(getEndTime())
				.append("\t").append(course)
				.append("\t").append(info).toString();
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

}
