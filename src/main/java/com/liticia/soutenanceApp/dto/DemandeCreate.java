package com.liticia.soutenanceApp.dto;

import lombok.Data;

@Data
public class DemandeCreate {
    private String firstName;
    private String lastName;
    private String email;
    private String document;
    private String city;
    private String speciality;
}
