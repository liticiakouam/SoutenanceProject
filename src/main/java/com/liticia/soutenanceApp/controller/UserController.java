package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.model.DemandeCompte;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.DemandRepository;
import com.liticia.soutenanceApp.service.CityService;
import com.liticia.soutenanceApp.service.RoleService;
import com.liticia.soutenanceApp.service.SpecialityService;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

        model.addAttribute("user", user);
        model.addAttribute("roleId", roleId);
        return "profile";
    }

    @GetMapping("/admin/homePage")
    public String getAdminHomePage(Model model) {
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();
        int professionals = userService.findProfessionals().size();
        int clients = userService.findClients().size();
        int cities = cityService.findAll().size();
        int specialities = specialityService.findAll().size();
        int demands = demandRepository.findAll().size();

        model.addAttribute("user", user);
        model.addAttribute("professionals", professionals);
        model.addAttribute("demands", demands);
        model.addAttribute("clients", clients);
        model.addAttribute("cities", cities);
        model.addAttribute("specialities", specialities);
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

}
