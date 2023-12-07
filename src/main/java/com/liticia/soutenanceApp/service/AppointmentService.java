package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment save(AppointmentCreate appointmentCreate, MultipartFile file, String document, long id) throws IOException;

    Page<Appointment> findPageByReportAndUser(Pageable pageable);

    List<Appointment> findAllByReportAndUser();

    Optional<Appointment> findById(long id);

    List<Appointment> findAppointmentByOldDate();

    List<Appointment> findAppointmentToComeByDate();

    void deleteAppointment(long id);

    List<Appointment> findRecentAppointmentDate();

}
