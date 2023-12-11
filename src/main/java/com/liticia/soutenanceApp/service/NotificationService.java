package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.ProfessionalCreate;
import com.liticia.soutenanceApp.model.Appointment;

import java.util.List;

public interface NotificationService {
    void sendConfirmationEmail(Appointment appointment);

    void sendForgotPasswordEmail(String toEmail, String emailBody);
    void sendSuccessfulRegistrationEmail(ProfessionalCreate professionalCreate);
}