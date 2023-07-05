package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.DemandeCreate;
import com.liticia.soutenanceApp.service.CityService;
import com.liticia.soutenanceApp.service.SpecialityService;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getAllClients(Model model) {
        model.addAttribute("demandCreate", new DemandeCreate());
        return "home";
    }

    @PostMapping("/demandCompte/add")
    public String save(@ModelAttribute("demandCreate") DemandeCreate demandeCreate,
                       @RequestParam("productImage") MultipartFile file,
                       @RequestParam("document")String document,
                       RedirectAttributes redirectAttributes) throws IOException {
        userService.saveProfessionalDemand(demandeCreate, file, document);
        redirectAttributes.addFlashAttribute("message", "Votre demande a bien été prise en compte nous reviendrons vers vous, Merci !!");

        return "redirect:/";
    }
}
