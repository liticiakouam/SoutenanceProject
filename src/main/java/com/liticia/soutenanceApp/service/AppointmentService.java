package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

public interface AppointmentService {
    void save(AppointmentCreate appointmentCreate, MultipartFile file, String document, long id) throws IOException;

    Optional<Appointment> findByUserCustomerAndCreatedAt(User user, Instant dateTime);

}
