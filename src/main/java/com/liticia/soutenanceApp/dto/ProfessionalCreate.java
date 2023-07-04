package com.liticia.soutenanceApp.dto;

import lombok.Data;

@Data
public class ProfessionalCreate {
    private String firstName;
    private String lastName;
    private String email;
    private long cityId;
    private long specialityId;
}
