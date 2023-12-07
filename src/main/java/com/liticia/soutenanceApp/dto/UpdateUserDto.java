package com.liticia.soutenanceApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private String details;
    private String address;
    private String email;
    private int phone;

}