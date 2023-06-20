package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.exception.UserNotFoundException;
import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.AvailabilityService;
import com.liticia.soutenanceApp.service.UserService;
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
import java.util.*;

import static com.liticia.soutenanceApp.utils.Week.getFullWeek;

@Controller
@RequestMapping("/professional")
public class AvailabiltyController {
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private UserService userService;

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

        Optional<User> optionalUser = userService.findById(SecurityUtils.getCurrentUserId());
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        AvailabilityResponse availabilities = availabilityService.getAvailabilities(startDate, optionalUser.get());
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

}
