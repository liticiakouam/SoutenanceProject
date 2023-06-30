package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.exception.AvailabilityException;
import com.liticia.soutenanceApp.exception.UserNotFoundException;
import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.AvailabilityService;
import com.liticia.soutenanceApp.service.ProfessionnalService;
import com.liticia.soutenanceApp.service.RoleService;
import com.liticia.soutenanceApp.service.UserService;
import com.liticia.soutenanceApp.utils.StartDayOfWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.*;
import java.util.*;

import static com.liticia.soutenanceApp.utils.StartDayOfWeek.getStartOfWeekDay;
import static com.liticia.soutenanceApp.utils.Week.getFullWeek;

@Controller
public class AvailabiltyController {
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private ProfessionnalService professionnalService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @GetMapping("/professional/availability")
    public String availability(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, Model model){
        return findPaginated(startDate, model);
    }


    private String findPaginated(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                 Model model
    ) {

        LocalDate date = LocalDate.now();
        LocalDate startOfWeek = getStartOfWeekDay(date);
        if (startDate.isBefore(startOfWeek)) {
            startDate = startOfWeek;
        }

        Optional<User> optionalUser = professionnalService.findById(SecurityUtils.getCurrentUserId());
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        AvailabilityResponse availabilities = availabilityService.getAvailabilities(startDate, optionalUser.get());
        long[][] availabilityArray = availabilities.getAvailabilities();
        List<LocalDate> fullWeek = getFullWeek(startDate);
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("roleId", roleId);
        model.addAttribute("user", user);
        model.addAttribute("availabilities", availabilityArray);
        model.addAttribute("fullWeek", fullWeek);
        model.addAttribute("startDate", startDate);

        return "availability";
    }

    @GetMapping("/professional/availabilityShowAdd")
    public String showAddForm(AvailabilityCreate availabilityCreate, Model model) throws ParseException {
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("user", user);
        model.addAttribute("roleId", roleId);
        model.addAttribute("availability", availabilityCreate);
        return "addAvailability";
    }

    @PostMapping("/professional/availability/add")
    public String save(@ModelAttribute("availability") AvailabilityCreate availabilityCreate) throws ParseException {
        availabilityService.saveAvailabilities(availabilityCreate);
        return "redirect:/professional/availability?startDate=2023-01-01";
    }

    @GetMapping("/availabilityId")
    public String findAvailability(@RequestParam("id") long id, Model model) {
        Optional<Availability> optionalAvailability = availabilityService.findById(id);
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("user", user);
        model.addAttribute("roleId", roleId);
        model.addAttribute("availability", optionalAvailability.get());
        model.addAttribute("appointment", new AppointmentCreate());
        return "motifrdv";
    }


}
