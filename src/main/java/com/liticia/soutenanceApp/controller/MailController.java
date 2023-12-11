package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.exception.EmailSendException;
import com.liticia.soutenanceApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class MailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/confirmationMail")
    public String getEmail(@RequestParam("emailPro") String emailPro,
                           @RequestParam("emailCli") String emailCli,
                           @RequestParam("pattern") String pattern,
                           @RequestParam("description") String description,
                           @RequestParam("date") String date,
                           @RequestParam("time") String time,
                           @RequestParam("firstnameCli") String firstnameCli,
                           @RequestParam("lastnameCli") String lastnameCli,
                           @RequestParam("firstnamePro") String firstnamePro,
                           @RequestParam("lastnamePro") String lastnamePro,
                           RedirectAttributes redirectAttributes) {
        try {
            List<String> emailsDestinataires = Arrays.asList(emailPro, emailCli);
            String textCli = "Nouveau Rendez-vous\n\n" +
                    "M/Mme " + firstnameCli + " " + lastnameCli + ", vous avez un rendez-vous avec " + lastnamePro + " " + firstnamePro + ", le\n" +
                    date + " à " + time + ".\n" +
                    "La raison de la viste est : " + pattern + "\n" +
                    "La description : " + description;

            String textPro = "Nouveau Rendez-vous\n\n" +
                    "M/Mme " + firstnamePro + " " + lastnamePro + ", vous avez un rendez-vous avec " + lastnameCli + " " + firstnameCli + ", le " + date + " à " + time + ".\n" +
                    "La raison est : " + pattern + "\n" +
                    "La description : " + description;
//            emailService.sendEmail(emailsDestinataires, textCli, textPro, emailCli, emailPro);
            redirectAttributes.addFlashAttribute("message", "Merci rendez-vous pris en compte !!!");

        } catch (EmailSendException e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenu lors de l'envoie du mail!!");
            return "redirect:/appointment/info";
        }

        return "redirect:/appointment/info";
    }
}
