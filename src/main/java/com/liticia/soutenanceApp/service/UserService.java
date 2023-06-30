package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.model.Role;

import java.util.Optional;

public interface UserService {
    Optional<Role> findByUsersId();
}
