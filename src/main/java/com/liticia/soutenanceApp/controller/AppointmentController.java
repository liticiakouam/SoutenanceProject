package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.dto.ReportCreate;
import com.liticia.soutenanceApp.exception.AppointmenNotFoundException;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.AppointmentService;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        Optional<Appointment> optionalAppointment = appointmentService.findByUserCustomerAndCreatedAt();

        if (optionalAppointment.isEmpty()) {
            throw new AppointmenNotFoundException();
        }

        model.addAttribute("appointment", optionalAppointment.get());
        return "confirmrdv";
    }

    @GetMapping("/appointment/toComplete")
    public String findIncompletedAppointment(@RequestParam("pageNumber") int pageNumber,
                                                      Model model
    ) {

        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<Appointment> page = appointmentService.findPageByReportAndUser(pageable);
        List<Appointment> appointments = page.getContent();
        List<Appointment> appointmentSize = appointmentService.findAllByReportAndUser();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("appointments", appointments);
        model.addAttribute("appointmentSize", appointmentSize.size());
        model.addAttribute("report", new ReportCreate());
        return "rdv";
    }

    @GetMapping("/appointment/{id}")
    public String findById(@PathVariable(value = "id") long id, Model model) {
        Optional<Appointment> appointment = appointmentService.findById(id);
        model.addAttribute("appointment", appointment);
        return "rdv";
    }

}
