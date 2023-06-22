package com.liticia.soutenanceApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreate {

    private long availabilityId;

    private long userPro;

    private String pattern;

    private String description;

    private String document;
}
