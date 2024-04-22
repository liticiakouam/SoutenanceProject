package com.liticia.soutenanceApp.dto;

import lombok.Data;

@Data
public class ProfessionalCreate {
    private String firstName;
    private String lastName;
    private String domain;
    private String email;
    private String city;
    private String speciality;
}
