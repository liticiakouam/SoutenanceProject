package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByUsersId();
}
