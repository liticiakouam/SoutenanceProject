package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAllByRolesIdOrderByCreatedAtDesc(pageable, 3);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> searchUser(String city, String speciality, String keyword) {
        return userRepository.searchUsers(city, speciality, keyword);
    }

    @Override
    public List<User> findUsers() {
        return null;
    }

}
