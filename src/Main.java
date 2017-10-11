import dhbw.timetable.rablabla.data.Appointment;
import dhbw.timetable.rablabla.data.DataImporter;
import dhbw.timetable.rablabla.data.DateUtilities;
import dhbw.timetable.rablabla.data.excpetions.NoConnectionException;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import static dhbw.timetable.rablabla.data.DataImporter.ImportWeekRange;

public class Main {

    private final static String[] test_urls = {
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhA_bi91ugPaHvrpxD-lcejo",
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhA_bi91ugPaHvrpxD-lcejo&day=9&month=12&year=2016&today=Heute&test=crap",

            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhAWXw8wYxzdc8a_Gx7NBrcf",
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhAWXw8wYxzdc8a_Gx7NBrcf&day=9&month=12&year=2016&today=Heute&test=crap",

            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhCdjALVplAQk2X4GqU-cLG6",
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhCdjALVplAQk2X4GqU-cLG6&day=9&month=12&year=2016&today=Heute&test=crap",

            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhCRWwKk8VgrnjjdW94d4cBX",
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhCRWwKk8VgrnjjdW94d4cBX&day=9&month=12&year=2016&today=Heute&test=crap",

            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhB0mnNDNmJOne3oQwbsmD-7",
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhB0mnNDNmJOne3oQwbsmD-7&day=9&month=12&year=2016&today=Heute&test=crap",

            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhCoRaEDgu02i2mi9JJCMzhf",
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhCoRaEDgu02i2mi9JJCMzhf&day=9&month=12&year=2016&today=Heute&test=crap",

            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhAFY7E6cFyNu-AINyZP2Og4",
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhAFY7E6cFyNu-AINyZP2Og4&day=9&month=12&year=2016&today=Heute&test=crap",

            "https://rapla.dhbw-karlsruhe.de/rapla?page=calendar&user=vollmer&file=tinf15b3",
            "https://rapla.dhbw-karlsruhe.de/rapla?page=calendar&user=vollmer&file=tinf15b3&day=26&month=6&year=2017&goto=Datum+anzeigen&test=crap",
    };

    public static void main(String[] args)  {
        unit_test(LocalDate.of(2013, 1, 1), LocalDate.of(2018, 1, 1));
    }

    private static void unit_test(LocalDate start, LocalDate end) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        System.out.println("Starting test from " + start.format(dtf) + " to " + end.format(dtf) +  "...");

        for(String url : test_urls) {
            System.out.print("Checking " + url + " ... ");
            try {
                Map<LocalDate, ArrayList<Appointment>> data = DataImporter.ImportWeekRange(start, end, url);
                System.out.println("SUCCESS!");
            } catch (NoConnectionException | MalformedURLException | IllegalAccessException e) {
                System.out.println("FAIL!");
                e.printStackTrace();
            }
        }

        System.out.println("Test successfully finished! :)");
    }

    private static void print(Map<LocalDate, ArrayList<Appointment>> data) {
        int size = 0;
        for (ArrayList<Appointment> week : data.values()) {
            size += week.size();
            for (Appointment a : week) {
                System.out.println(a);
            }
        }
        System.out.println("Size: " + size);
    }
}
