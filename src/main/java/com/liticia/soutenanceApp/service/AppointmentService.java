package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.model.Appointment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AppointmentService {
    void save(Appointment appointment);
}
