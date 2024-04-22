package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.dto.ReportCreate;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.Report;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.AppointmentRepository;
import com.liticia.soutenanceApp.repository.ReportRepository;
import com.liticia.soutenanceApp.repository.RoleRepository;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
    private ReportRepository reportRepository;
    private AppointmentRepository appointmentRepository;
    private RoleRepository roleRepository;
    private SecurityUtils securityUtils;

    @Override
    public void save(ReportCreate reportCreate, long id) {
        long roleId = roleRepository.findByUsersId(securityUtils.getCurrentUserId()).get().getId();
        Appointment appointment = appointmentRepository.findById(id).get();

        Report report = new Report();
        report.setNote(reportCreate.getNote());
        report.setLevelOfVisibility(reportCreate.getLevelOfVisibility());
        report.setCreatedAt(Instant.now());

        reportRepository.save(report);

        appointment.setId(id);
        if (roleId == 2) {
            appointment.setReport(report);
        } else if (roleId == 3) {
            appointment.setReportPro(report);
        }
        appointmentRepository.save(appointment);
    }
}
