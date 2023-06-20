package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;

import java.text.ParseException;
import java.time.LocalDate;

public interface AvailabilityService {
    void saveAvailabilities(AvailabilityCreate availabilityCreate) throws ParseException;

    AvailabilityResponse getAvailabilities(LocalDate startDate);
}
