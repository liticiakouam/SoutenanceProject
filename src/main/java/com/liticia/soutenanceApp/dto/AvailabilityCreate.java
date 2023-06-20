package com.liticia.soutenanceApp.dto;

import com.liticia.soutenanceApp.model.Schedule;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class AvailabilityCreate {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Schedule schedule;
}
