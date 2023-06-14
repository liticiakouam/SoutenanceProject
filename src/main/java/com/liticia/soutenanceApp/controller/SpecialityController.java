package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.service.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SpecialityController {

    @Autowired
    private SpecialityService specialityService;

    @GetMapping("/spe")
    public String getSpecialities(Model model) {
        List<Speciality> specialityList = specialityService.findAll();
        model.addAttribute("specialities", specialityList);
        return "/users?pageNumber=1";
    }

}
