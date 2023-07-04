package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.ProfessionalCreate;
import com.liticia.soutenanceApp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById();
    List<User> findClients();
    List<User> findProfessionals();
    void saveProfessional(ProfessionalCreate professionalCreate);

}
