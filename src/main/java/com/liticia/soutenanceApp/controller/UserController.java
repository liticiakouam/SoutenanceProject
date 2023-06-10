package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String getAllUsers() {
//        List<User> users = userService.getAllUser();
//        return new ResponseEntity<>(users, HttpStatus.OK);
        return "rdv";
    }
}
