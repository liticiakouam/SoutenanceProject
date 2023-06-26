package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.dto.ReportCreate;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.Report;
import com.liticia.soutenanceApp.repository.AppointmentRepository;
import com.liticia.soutenanceApp.repository.ReportRepository;
import com.liticia.soutenanceApp.service.ReportService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final AppointmentRepository appointmentRepository;

    public ReportServiceImpl(ReportRepository reportRepository, AppointmentRepository appointmentRepository) {
        this.reportRepository = reportRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void save(ReportCreate reportCreate, long id) {
        Report report = new Report();
        report.setNote(reportCreate.getNote());
        report.setLevelOfVisibility(reportCreate.getLevelOfVisibility());
        report.setCreatedAt(Instant.now());

        reportRepository.save(report);

        Appointment appointment = new Appointment();
        appointment.setReport(report);
        appointment.setId(id);
        appointmentRepository.save(appointment);
    }
}
