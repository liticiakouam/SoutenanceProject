package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CityService {
    List<City> findAll();
}
