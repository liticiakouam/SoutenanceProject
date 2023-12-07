package com.liticia.soutenanceApp.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}