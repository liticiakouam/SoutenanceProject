package com.liticia.soutenanceApp.dto;

import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import lombok.Data;

@Data
public class SearchUserDto {
    private String keyword;
    private int city;
    private int speciality;
}
