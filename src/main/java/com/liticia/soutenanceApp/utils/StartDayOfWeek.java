package com.liticia.soutenanceApp.utils;

import java.time.*;

public class StartDayOfWeek {
    public static LocalDate getStartOfWeekDay(LocalDate date) {
        ZoneId zone = ZoneId.systemDefault();

        LocalTime time = LocalTime.MIDNIGHT;
        ZonedDateTime zdt = ZonedDateTime.of(date, time, zone);

        DayOfWeek dayOfWeek = zdt.getDayOfWeek();

        int daysUntilStartOfWeek = dayOfWeek.getValue() - 1;

        return date.minusDays(daysUntilStartOfWeek);
    }
}
