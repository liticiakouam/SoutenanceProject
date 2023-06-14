package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/users?pageNumber=1")
    public String getCities(Model model) {
        List<City> cityList = cityService.findAll();
        model.addAttribute("cities", cityList);
        return "users";
    }

}
