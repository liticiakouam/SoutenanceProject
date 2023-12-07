package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.dto.ReportCreate;
import com.liticia.soutenanceApp.exception.AppointmenNotFoundException;
import com.liticia.soutenanceApp.exception.AvailabilityException;
import com.liticia.soutenanceApp.exception.EmailSendException;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ProfessionnalService professionnalService;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/appointment/add")
    public String save(@ModelAttribute("appointment") AppointmentCreate appointmentCreate,
                       @RequestParam("productImage")MultipartFile file,
                       @RequestParam("document")String document,
                       @RequestParam("id") long id,
                       RedirectAttributes redirectAttributes,
                       Model model) throws IOException {
        Appointment appointment = null;
        try {
            appointment = appointmentService.save(appointmentCreate, file, document, id);
            notificationService.sendConfirmationEmail(appointment);

            return "redirect:/appointment/info?id="+appointment.getId();
        } catch (AvailabilityException ex) {
                redirectAttributes.addFlashAttribute("message", "Désolé, désolé cet plage n'est plus disponible. Veuillez en choisir une nouvelle");
            Optional<Availability> optionalAvailability = availabilityService.findById(id);
            model.addAttribute("availability", optionalAvailability.get());

            return "redirect:/availabilityId?id="+id;
        } catch (EmailSendException e) {
            assert appointment != null;
            return "redirect:/appointment/info?id="+appointment.getId();
        }
    }

    @GetMapping("/appointment/info")
    public String findAppointment(@RequestParam("id") long id, Model model,  RedirectAttributes redirectAttributes) {
        try {
            Optional<Appointment> optionalAppointment = appointmentService.findById(id);
            long roleId = roleService.findByUsersId().get().getId();
            User user = userService.findById().get();

            model.addAttribute("roleId", roleId);
            model.addAttribute("user", user);
            model.addAttribute("appointment", optionalAppointment.get());
            return "confirmrdv";

        } catch (AppointmenNotFoundException ex) {
            return "serverError";
        }
    }

    @GetMapping("/appointment/toComplete")
    public String findIncompletedAppointment(Model model) {
        List<Appointment> appointments = appointmentService.findAllByReportAndUser();
        List<Appointment> appointmentPasser = appointmentService.findAppointmentByOldDate();
        List<Appointment> appointmentToCome = appointmentService.findAppointmentToComeByDate();
        List<Appointment> appointmentNext = appointmentService.findRecentAppointmentDate();
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("user", user);
        model.addAttribute("appointments", appointments);
        model.addAttribute("roleId", roleId);
        model.addAttribute("appointmentNext", appointmentNext);
        model.addAttribute("appointmentNextSize", appointmentNext.size());
        model.addAttribute("appointmentSize", appointments.size());
        model.addAttribute("appointmentPasserSize", appointmentPasser.size());
        model.addAttribute("appointmentToCome", appointmentToCome.size());
        model.addAttribute("report", new ReportCreate());

        return "rdv";
    }

    @GetMapping("/appointment/passer")
    public String findByOldAppointmentByDate(Model model) {
        List<Appointment> appointments = appointmentService.findAppointmentByOldDate();
        List<Appointment> appointmentToComplete = appointmentService.findAllByReportAndUser();
        List<Appointment> appointmentToCome = appointmentService.findAppointmentToComeByDate();
        List<Appointment> appointmentNext = appointmentService.findRecentAppointmentDate();
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("user", user);
        model.addAttribute("appointments", appointments);
        model.addAttribute("roleId", roleId);
        model.addAttribute("appointmentSize", appointments.size());
        model.addAttribute("appointmentNext", appointmentNext);
        model.addAttribute("appointmentNextSize", appointmentNext.size());
        model.addAttribute("appointmentToComplet", appointmentToComplete.size());
        model.addAttribute("appointmentToCome", appointmentToCome.size());

        return "rdvPasser";
    }

    @GetMapping("/appointment/toCome")
    public String findAppointmentToComeByDate(Model model) {
        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        List<Appointment> appointmentToComplete = appointmentService.findAllByReportAndUser();
        List<Appointment> appointmentPasser = appointmentService.findAppointmentByOldDate();
        List<Appointment> appointmentNext = appointmentService.findRecentAppointmentDate();
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("user", user);
        model.addAttribute("appointments", appointments);
        model.addAttribute("roleId", roleId);
        model.addAttribute("appointmentNext", appointmentNext);
        model.addAttribute("appointmentNextSize", appointmentNext.size());
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
