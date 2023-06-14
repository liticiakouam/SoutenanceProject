package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.repository.SpecialityRepository;
import com.liticia.soutenanceApp.service.SpecialityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialityServiceImpl implements SpecialityService {

    private final SpecialityRepository specialityRepository;

    public SpecialityServiceImpl(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }


    @Override
    public List<Speciality> findAll() {
        return specialityRepository.findAll();
    }
}
