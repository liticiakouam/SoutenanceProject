package com.liticia.soutenanceApp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class AvailabilityResponse {
    private LocalDate previousStartDate;
    private LocalDate nextStartDate;
    private long[][] availabilities = new long[7][9];
}
