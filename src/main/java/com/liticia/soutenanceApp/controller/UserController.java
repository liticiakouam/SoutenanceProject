package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.ForgotPasswordForm;
import com.liticia.soutenanceApp.dto.UpdateUserDto;
import com.liticia.soutenanceApp.exception.EmailSendException;
import com.liticia.soutenanceApp.model.DemandeCompte;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.DemandRepository;
import com.liticia.soutenanceApp.service.*;
import com.liticia.soutenanceApp.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

import static com.liticia.soutenanceApp.utils.TokenGenerator.generateToken;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SpecialityService specialityService;
    @Autowired
    private CityService cityService;
    @Autowired
    private DemandRepository demandRepository;

    @GetMapping("/user/profile")
    public String getUserInfo(Model model) {
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("userProfile", new UpdateUserDto());
        model.addAttribute("user", user);
        model.addAttribute("roleId", roleId);
        return "profile";
    }
    @GetMapping("/user/updateProfile")
    public String getUserUpdateInfo(Model model) {
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("updateUserDto", new UpdateUserDto());
        model.addAttribute("user", user);
        model.addAttribute("roleId", roleId);
        return "profileUpd";
    }

    @PostMapping("/user/updateProfile")
    public String getUpdateInfo(@ModelAttribute("updateUserDto") UpdateUserDto updateUserDto, Model model) {
//        long roleId = roleService.findByUsersId().get().getId();
//        User user = userService.findById().get();
        userService.updateUserInformations(updateUserDto);

//        model.addAttribute("user", user);
//        model.addAttribute("roleId", roleId);
        return "redirect:/user/updateProfile";
    }

}
