package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.dto.ProfessionalCreate;
import com.liticia.soutenanceApp.exception.EmailSendException;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.service.EmailService;
import com.liticia.soutenanceApp.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private EmailService emailService;

    @Override
    public void sendConfirmationEmail(Appointment appointment) {
        String subject = "Confirmation de rendez-vous";
        String customerText = "Nouveau Rendez-vous\n\n" +
                "M/Mme " + appointment.getUserCustomer().getFirstName() + " " + appointment.getUserCustomer().getLastName() + ", vous avez un rendez-vous avec " + appointment.getUserPro().getFirstName() + " " + appointment.getUserPro().getLastName() + ". Le" +
                appointment.getAvailability().getDate() + " à " + appointment.getAvailability().getStartTime() + ".\n" +
                "La raison est : " + appointment.getPattern() + "\n" +
                "La description : " + appointment.getDescription();
        String professionalText = "Nouveau Rendez-vous\n\n"+
                "M/Mme " + appointment.getUserPro().getFirstName() + " " + appointment.getUserPro().getLastName()+ " , vous avez un rendez-vous avec " + appointment.getUserCustomer().getFirstName() + " " + appointment.getUserCustomer().getLastName() + ". \nLe " +
                appointment.getAvailability().getDate() + " à " + appointment.getAvailability().getStartTime() + ".\n" +
                "La raison est : " + appointment.getPattern() + "\n" +
                "La description : " + appointment.getDescription();

        try {
            emailService.sendEmail(appointment.getUserPro().getEmail(), subject, professionalText);
            emailService.sendEmail(appointment.getUserCustomer().getEmail(), subject, customerText);
        } catch (MailException ex) {
            throw new EmailSendException();
        }
    }


    @Override
    public void sendForgotPasswordEmail(String toEmail, String resetPasswordLink) {
        String subject = "Réinitialisation du mot de passe";
        String emailBody = "Pour réinitialiser votre mot de passe entrer le code suivant : " + resetPasswordLink + ".\nCe code est valide pendant 10 minutes";

        try {
            emailService.sendEmail(toEmail, subject ,emailBody);
        } catch (MailException ex) {
            throw new EmailSendException();
        }
    }

    @Override
    public void sendSuccessfulRegistrationEmail(ProfessionalCreate professionalCreate) {
        String subject = "Création de compte valider";
        String text = professionalCreate.getFirstName()+ " "+ professionalCreate.getLastName()+ "\n\n Bienvenue chez Easy Appointment, votre compte a été créer avec succès.\n\nVeuillez cliquer pour réinitialiser votre mot de passe et accéder à votre compte" + " " +
                "http://localhost:8080/"
                ;

        try {
            emailService.sendEmail(professionalCreate.getEmail(), subject ,text);
        } catch (MailException ex) {
            throw new EmailSendException();
        }
    }
}