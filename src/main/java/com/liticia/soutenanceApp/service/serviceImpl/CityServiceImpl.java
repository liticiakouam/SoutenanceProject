package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.repository.CityRepository;
import com.liticia.soutenanceApp.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }
}
