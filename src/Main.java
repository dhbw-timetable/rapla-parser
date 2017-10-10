import dhbw.timetable.rablabla.data.Appointment;
import dhbw.timetable.rablabla.data.BaseURL;
import dhbw.timetable.rablabla.data.DataImporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import static dhbw.timetable.rablabla.data.DataImporter.ImportDateRange;

public class Main {
    public static void main(String[] args) {
        Map<LocalDate, ArrayList<Appointment>> data = DataImporter.ImportDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2018, 1, 1), BaseURL.STUTTGART, "key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhA_bi91ugPaHvrpxD-lcejo");
        int size = 0;
        for (ArrayList<Appointment> week : data.values()) {
            size += week.size();
            for (Appointment a : week) {
                System.out.println(a);
            }
        }
    }
}
