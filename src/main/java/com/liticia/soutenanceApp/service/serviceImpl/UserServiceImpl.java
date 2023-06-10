package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
