package com.liticia.soutenanceApp.utils;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class Week {
    public static List<LocalDate> getFullWeek(LocalDate date) {
        ZoneId zone = ZoneId.systemDefault();

        LocalTime time = LocalTime.MIDNIGHT;
        ZonedDateTime zdt = ZonedDateTime.of(date, time, zone);

        DayOfWeek dayOfWeek = zdt.getDayOfWeek();

        int daysUntilStartOfWeek = dayOfWeek.getValue() - 1;
        LocalDate startOfWeek = date.minusDays(daysUntilStartOfWeek);

        List<LocalDate> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);
            week.add(currentDate);
        }

        return week;
    }
}
