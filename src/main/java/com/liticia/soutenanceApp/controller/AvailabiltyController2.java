package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;

@RestController
public class AvailabiltyController2 {
    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping("/availabilities")
    public AvailabilityResponse show(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate){
        return availabilityService.getAvailabilities(startDate);
    }


}
