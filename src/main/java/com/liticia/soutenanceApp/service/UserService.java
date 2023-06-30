package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById();
}
