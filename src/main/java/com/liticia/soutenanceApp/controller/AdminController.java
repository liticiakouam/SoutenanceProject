package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.DemandeCreate;
import com.liticia.soutenanceApp.dto.ProfessionalCreate;
import com.liticia.soutenanceApp.exception.EmailAlreadyExistException;
import com.liticia.soutenanceApp.exception.EmailSendException;
import com.liticia.soutenanceApp.model.DemandeCompte;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private DemandService demandService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/demandCompte/add")
    public String save(@ModelAttribute("demandCreate") DemandeCreate demandeCreate,
                       @RequestParam("productImage") MultipartFile file,
                       @RequestParam("document")String document,
                       RedirectAttributes redirectAttributes) throws IOException {
        userService.saveProfessionalDemand(demandeCreate, file, document);
        redirectAttributes.addFlashAttribute("message", "Votre demande a bien été prise en compte nous reviendrons vers vous, Merci !!");

        return "redirect:/";
    }

    @GetMapping("/admin/homePage")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAdminHomePage(Model model) {
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();
        int professionals = userService.findProfessionals().size();
        List<User> clients = userService.findClients();
        List<DemandeCompte> demands = demandService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("professionals", professionals);
        model.addAttribute("professional", new ProfessionalCreate());
        model.addAttribute("demandsSize", demands.size());
        model.addAttribute("demands", demands);
        model.addAttribute("clientsSize", clients.size());
        model.addAttribute("clients", clients);
        model.addAttribute("roleId", roleId);
        return "adminHome";
    }

    @GetMapping("/admin/clients")
    public String getAllClients(Model model) {
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();
        List<User> users = userService.findClients();

        model.addAttribute("user", user);
        model.addAttribute("users", users);
        model.addAttribute("roleId", roleId);
        return "adminClientList";
    }


    @GetMapping("/admin/professionals")
    public String getAllProfessionals(Model model) {
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();
        List<User> users = userService.findProfessionals();

        model.addAttribute("user", user);
        model.addAttribute("professional", new ProfessionalCreate());
        model.addAttribute("users", users);
        model.addAttribute("roleId", roleId);
        return "adminProList";
    }

    @PostMapping("/admin/professional/add")
    public String saveProfessionals(@ModelAttribute("professional")ProfessionalCreate professionalCreate, RedirectAttributes redirectAttributes, Model model) {
        try {
            userService.saveProfessional(professionalCreate);

            notificationService.sendSuccessfulRegistrationEmail(professionalCreate);
            redirectAttributes.addFlashAttribute("timeOutMessage", "Veuillez patienter le mail est encours d'envoie. Merci");
            return "redirect:/admin/professionals";

        } catch (EmailSendException e) {
            return "redirect:/admin/homePage";
        } catch (EmailAlreadyExistException e ) {

            redirectAttributes.addFlashAttribute("email",
                    "Il existe déjà un compte avec cet adresse email");
            model.addAttribute("professional", professionalCreate);

            if(result.hasErrors()){
                result.rejectValue("email", null,
                        "Il existe déjà un compte avec cet adresse email.");
            }

            return "redirect:/admin/homePage";

        }
    }

    @GetMapping("/deleteDemand/{id}")
    public String deleteDemand(@PathVariable long id) {
        demandService.delete(id);
        return "redirect:/admin/homePage";
    }
}
