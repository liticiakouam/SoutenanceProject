package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AppointmentService {
    void save(AppointmentCreate appointmentCreate, MultipartFile file, String document, long id) throws IOException;
}
