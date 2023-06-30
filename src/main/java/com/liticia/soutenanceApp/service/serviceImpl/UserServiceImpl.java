package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById() {
        return userRepository.findById(SecurityUtils.getCurrentUserId());
    }
}
