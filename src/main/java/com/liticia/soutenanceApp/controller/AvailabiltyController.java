package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/professional")
public class AvailabiltyController {
    @Autowired
    private AvailabilityService availabilityService;

//    @GetMapping("/availability")
//    public String availability(){
//        return "availability";
//    }

    @GetMapping("/availability")
    public String availability(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, Model model){
        return findPaginated(startDate, model);
    }


    private String findPaginated(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                 Model model
    ) {

        LocalDate startDayOfWeek = LocalDate.now();

        if (startDate.isBefore(startDayOfWeek)) {
            startDate = startDayOfWeek;
        }
        AvailabilityResponse availabilities = availabilityService.getAvailabilities(startDate);
        long[][] availabilityArray = availabilities.getAvailabilities();
        List<LocalDate> fullWeek = getFullWeek(startDate);

        model.addAttribute("availabilities", availabilityArray);
        model.addAttribute("fullWeek", fullWeek);
        model.addAttribute("startDate", startDate);

        return "availability";
    }

    @GetMapping("/availabilityShowAdd")
    public String showAddForm(@ModelAttribute("availability") AvailabilityCreate availabilityCreate){
        return "addAvailability";
    }

    @PostMapping("/availability/add")
    public String save(@ModelAttribute("availability") AvailabilityCreate availabilityCreate) throws ParseException {
        LocalDate date = availabilityCreate.getDate();
        availabilityService.saveAvailabilities(availabilityCreate);
        return "redirect:/professional/availability?startDate=2023-01-01";
    }

    private List<LocalDate> getFullWeek(LocalDate date) {
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
