package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.User;
import org.springframework.data.util.Pair;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AvailabilityService {
    void saveAvailabilities(AvailabilityCreate availabilityCreate) throws ParseException;

    List<Pair<LocalTime, LocalTime>> generateTimes(LocalTime startTime, LocalTime endTime);

    AvailabilityResponse getAvailabilities(LocalDate startDate, User user);

    Optional<Availability> findById(long id);
}
