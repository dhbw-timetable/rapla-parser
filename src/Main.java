import dhbw.timetable.rablabla.data.Appointment;
import dhbw.timetable.rablabla.data.BaseURL;
import dhbw.timetable.rablabla.data.DataImporter;
import dhbw.timetable.rablabla.data.excpetions.NoConnectionException;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import static dhbw.timetable.rablabla.data.DataImporter.ImportDateRange;

public class Main {
    public static void main(String[] args) throws NoConnectionException, MalformedURLException, IllegalAccessException {
        Map<LocalDate, ArrayList<Appointment>> data = DataImporter.ImportDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2018, 1, 1),
                "https://rapla.dhbw-karlsruhe.de/rapla?page=CALENDAR&user=vollmer&file=tinf15b3&today=Heute");
        int size = 0;
        for (ArrayList<Appointment> week : data.values()) {
            size += week.size();
            for (Appointment a : week) {
                System.out.println(a);
            }
        }

        System.out.println("SIZE: " + size);
    }
}
