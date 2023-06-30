package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.RoleService;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/user/profile")
    public String getUserInfo(Model model) {
        long roleId = roleService.findByUsersId().get().getId();
        User user = userService.findById().get();

        model.addAttribute("user", user);
        model.addAttribute("roleId", roleId);
        return "profile";
    }

}
