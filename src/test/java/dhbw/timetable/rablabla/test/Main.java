package dhbw.timetable.rablabla.test;

import dhbw.timetable.rablabla.data.*;
import dhbw.timetable.rablabla.data.exceptions.NoConnectionException;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class Main {

    private static final boolean debugData = false;

    private final static String[] test_urls = {
            "http://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhA_bi91ugPaHvrpxD-lcejo",
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhA_bi91ugPaHvrpxD-lcejo&day=9&month=12&year=2016&today=Heute&test=crap",

            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhAWXw8wYxzdc8a_Gx7NBrcf",
            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhAWXw8wYxzdc8a_Gx7NBrcf&day=9&month=12&year=2016&today=Heute&test=crap",

            "https://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhCdjALVplAQk2X4GqU-cLG6",
            "http://rapla.dhbw-stuttgart.de/rapla?key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhCdjALVplAQk2X4GqU-cLG6&day=9&month=12&year=2016&today=Heute&test=crap",

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
        unit_test(LocalDate.of(2016, 12, 1), LocalDate.of(2017, 1, 1));
    }

    private static void unit_test(LocalDate start, LocalDate end) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        System.out.println("Starting test from " + start.format(dtf) + " to " + end.format(dtf) +  "...");
        boolean error = false;
        for(String url : test_urls) {
            System.out.print("Checking " + url + " ... ");
            try {
                Map<LocalDate, ArrayList<Appointment>> data = DataImporter.ImportWeekRange(start, end, url);
                Map<TimelessDate, ArrayList<BackportAppointment>> backportData = DataImporter.Backport.ImportWeekRange(
                        DateUtilities.ConvertToCalendar(start), DateUtilities.ConvertToCalendar(end), url);
                if (checkEquality(data, backportData)) {
                    System.out.println("SUCCESS!");
                } else {
                    error = true;
                    System.out.println("INVALID!");
                }
                if (debugData) {
                    print(data);
                    print(backportData);
                }
            } catch (NoConnectionException | MalformedURLException | IllegalAccessException e) {
                error = true;
                System.out.println("FAIL!");
                e.printStackTrace();
            }
        }
        if (error) {
            System.out.println("Main finished with errors :(");
        } else {
            System.out.println("Main successfully finished! :)");
        }
    }

    private static boolean checkEquality(Map<LocalDate, ArrayList<Appointment>> data, Map<TimelessDate, ArrayList<BackportAppointment>> backportData) {
        if (data == null || backportData == null || data.size() != backportData.size()) {
            return false;
        }

        for (LocalDate dateKey : data.keySet()) {
            ArrayList<Appointment> list1 = data.get(dateKey);
            ArrayList<BackportAppointment> list2 = backportData.get(DateUtilities.ConvertToCalendar(dateKey));

            if (list1 == null || list2 == null || list1.size() != list2.size()) {
                return false;
            }
            for(int i = 0; i < list1.size(); i++) {
                Appointment a1 = list1.get(i);
                BackportAppointment a2 = list2.get(i);

                if(!(a1.getDate().equals(a2.getDate()) && a1.getStartTime().equals(a2.getStartTime())
                        && a1.getEndTime().equals(a2.getEndTime()) && a1.getCourse().equals(a2.getCourse())
                        && a1.getInfo().equals(a2.getInfo()))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void print(Map data) {
        int size = 0;
        for (Object oWeek : data.values()) {
            ArrayList week = (ArrayList) oWeek;
            size += week.size();
            for (Object a : week) {
                System.out.println(a);
            }
        }
        System.out.println("Size: " + size);
    }

}
