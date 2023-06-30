package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.ReportCreate;
import com.liticia.soutenanceApp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping("/report/add/{id}")
    public String save(@ModelAttribute("report") ReportCreate reportCreate, @PathVariable("id") long id) {
        reportService.save(reportCreate, id);
        return "redirect:/appointment/toComplete";
    }
}
