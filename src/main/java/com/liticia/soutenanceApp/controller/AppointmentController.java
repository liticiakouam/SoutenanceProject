package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.exception.AppointmenNotFoundException;
import com.liticia.soutenanceApp.exception.UserNotFoundException;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.AvailabilityRepository;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.AppointmentService;
import com.liticia.soutenanceApp.service.AvailabilityService;
import com.liticia.soutenanceApp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.liticia.soutenanceApp.utils.Week.getFullWeek;

@Controller
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @PostMapping("/appointment/add")
    public String save(@ModelAttribute("appointment") AppointmentCreate appointmentCreate,
                       @RequestParam("productImage")MultipartFile file,
                       @RequestParam("document")String document, @RequestParam("id") long id) throws IOException {
        appointmentService.save(appointmentCreate, file, document, id);
        return "redirect:/appointment/info";
    }

    @GetMapping("/appointment/info")
    public String findAppointment(Model model) {
        Instant date = Instant.now();
        Optional<User> userOptional = userService.findById(SecurityUtils.getCurrentUserId());
        Optional<Appointment> optionalAppointment = appointmentService.findByUserCustomerAndCreatedAt(userOptional.get(), date);

        if (optionalAppointment.isEmpty()) {
            throw new AppointmenNotFoundException();
        }

        model.addAttribute("appointment", optionalAppointment.get());
        return "confirmrdv";
    }

}
