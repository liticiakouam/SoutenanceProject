package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.dto.ReportCreate;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.Report;
import com.liticia.soutenanceApp.repository.AppointmentRepository;
import com.liticia.soutenanceApp.repository.ReportRepository;
import com.liticia.soutenanceApp.repository.RoleRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.ReportService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final AppointmentRepository appointmentRepository;
    private final RoleRepository roleRepository;

    public ReportServiceImpl(ReportRepository reportRepository, AppointmentRepository appointmentRepository, RoleRepository roleRepository) {
        this.reportRepository = reportRepository;
        this.appointmentRepository = appointmentRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(ReportCreate reportCreate, long id) {
        long roleId = roleRepository.findByUsersId(SecurityUtils.getCurrentUserId()).get().getId();

        Report report = new Report();
        report.setNote(reportCreate.getNote());
        report.setLevelOfVisibility(reportCreate.getLevelOfVisibility());
        report.setCreatedAt(Instant.now());

        reportRepository.save(report);

        Appointment appointment = new Appointment();
        if (roleId == 2) {
            appointment.setReport(report);
        } else if (roleId == 3) {
            appointment.setReportPro(report);
        }
        appointment.setId(id);
        appointmentRepository.save(appointment);
    }
}
