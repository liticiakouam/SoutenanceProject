package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.User;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    void saveAvailabilities(AvailabilityCreate availabilityCreate) throws ParseException;

    AvailabilityResponse getAvailabilities(LocalDate startDate, User user);
}
