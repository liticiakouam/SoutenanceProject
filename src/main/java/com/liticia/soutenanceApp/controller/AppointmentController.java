package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.dto.ReportCreate;
import com.liticia.soutenanceApp.exception.AppointmenNotFoundException;
import com.liticia.soutenanceApp.model.Appointment;
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
    public String findIncompletedAppointment(Model model) {
        List<Appointment> appointments = appointmentService.findAllByReportAndUser();
        List<Appointment> appointmentPasser = appointmentService.findAppointmentByOldDate();
        List<Appointment> appointmentToCome = appointmentService.findAppointmentToComeByDate();

        model.addAttribute("appointments", appointments);
        model.addAttribute("appointmentSize", appointments.size());
        model.addAttribute("appointmentPasserSize", appointmentPasser.size());
        model.addAttribute("appointmentToCome", appointmentToCome.size());
        model.addAttribute("report", new ReportCreate());

        return "rdv";
    }

    @GetMapping("/appointment/{id}")
    public String findById(@PathVariable(value = "id") long id, Model model) {
        Optional<Appointment> appointment = appointmentService.findById(id);
        model.addAttribute("appointment", appointment);
        return "rdv";
    }

    @GetMapping("/appointment/passer")
    public String findByOldAppointmentByDate(Model model) {
        List<Appointment> appointments = appointmentService.findAppointmentByOldDate();
        List<Appointment> appointmentToComplete = appointmentService.findAllByReportAndUser();
        List<Appointment> appointmentToCome = appointmentService.findAppointmentToComeByDate();

        model.addAttribute("appointments", appointments);
        model.addAttribute("appointmentSize", appointments.size());
        model.addAttribute("appointmentToComplet", appointmentToComplete.size());
        model.addAttribute("appointmentToCome", appointmentToCome.size());

        return "rdvPasser";
    }

    @GetMapping("/appointment/toCome")
    public String findAppointmentToComeByDate(Model model) {
        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        List<Appointment> appointmentToComplete = appointmentService.findAllByReportAndUser();
        List<Appointment> appointmentPasser = appointmentService.findAppointmentByOldDate();

        model.addAttribute("appointments", appointments);
        model.addAttribute("appointmentSize", appointments.size());
        model.addAttribute("appointmentToComplet", appointmentToComplete.size());
        model.addAttribute("appointmentPasser", appointmentPasser.size());

        return "rdvAvenir";
    }

    @GetMapping("appointment/deleted/{id}")
    public String deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointment/toCome";
    }

}
