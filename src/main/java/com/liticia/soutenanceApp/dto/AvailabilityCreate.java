package com.liticia.soutenanceApp.dto;

import com.liticia.soutenanceApp.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class AvailabilityCreate {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Schedule schedule;
}
