package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.DemandeCreate;
import com.liticia.soutenanceApp.dto.CustomerDto;
import com.liticia.soutenanceApp.dto.ForgotPasswordForm;
import com.liticia.soutenanceApp.exception.EmailSendException;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.NotificationService;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.liticia.soutenanceApp.utils.TokenGenerator.generateToken;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("demandCreate", new DemandeCreate());
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new CustomerDto());
        return "register";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute("user") CustomerDto customerDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(customerDto.getEmail());

        if(existingUser != null){
            result.rejectValue("email", null,
                    "Il existe déjà un compte avec cet adresse email.");
        }

        if(result.hasErrors()){
            model.addAttribute("user", customerDto);
            return "/register";
        }
        userService.saveCustomer(customerDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/after-login")
    public String afterLogin(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/homePage";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"))) {
            return "redirect:/users?pageNumber=1";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSIONNEL"))) {
            return "redirect:/appointment/toComplete";
        } else {
            return "redirect:/after-login";
        }
    }


    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("forgotPasswordForm", new ForgotPasswordForm());
        return "passwordResetEmail";
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(@ModelAttribute("forgotPasswordForm") ForgotPasswordForm forgotPasswordForm,
                                            RedirectAttributes redirectAttributes) {
        User user = userService.findUserByEmail(forgotPasswordForm.getEmail());
        if (user == null) {
            redirectAttributes.addFlashAttribute("email", "Adresse email introuvable!");
            return "redirect:/forgot-password";
        }

        String token = generateToken();
        userService.createPasswordResetTokenForUser(user, token);

        try {
            notificationService.sendForgotPasswordEmail(user.getEmail(), token);
            redirectAttributes.addFlashAttribute("successMessage", "Un mail vous a été envoyé veuillez suivre les instructions.");
            return "redirect:/forgot-password/number";
        } catch (EmailSendException ex) {
            redirectAttributes.addFlashAttribute("timeOutMessage", "Veuillez patienter le mail est encours d'envoie, réessayer s'il prend trop de temps.");
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/forgot-password/number")
    public String showValidationCodePage() {
        return "passwordResetNumber";
    }

    @PostMapping("/forgot-password/number")
    public String saveValidationCodePage(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        boolean isValidToken = userService.validatePasswordResetToken(token);
        if (isValidToken) {
            model.addAttribute("token", token);
            return "resetPassword";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "code erroné.");
            return "redirect:/forgot-password/number";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        boolean isValidToken = userService.validatePasswordResetToken(token);
        if (isValidToken) {
            model.addAttribute("token", token);
            return "resetPassword";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "code erroné.");
            return "redirect:/forgot-password/number";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPasswordForm(@RequestParam("token") String token,
                                           @RequestParam("password") String password,
                                           @RequestParam("confirmPassword") String confirmPassword,
                                           RedirectAttributes redirectAttributes) {
        boolean isValidToken = userService.validatePasswordResetToken(token);
        if (!isValidToken) {
            redirectAttributes.addFlashAttribute("errorMessage", "code erroné.");
            return "redirect:/forgot-password/number";
        }
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Désolé, les mots de passe ne correspondent pas");
            return "redirect:/reset-password?token=" + token;
        }
        User user = userService.getUserByPasswordResetToken(token);
        userService.changeUserPassword(user, password);
        userService.deletePasswordResetToken(token);
        redirectAttributes.addFlashAttribute("successMessage", "Votre mot de passe a été réinitialiser avec succès");
        return "redirect:/login";
    }
}
