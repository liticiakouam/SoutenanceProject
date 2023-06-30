package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.model.Role;
import com.liticia.soutenanceApp.repository.RoleRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private RoleRepository roleRepository;

    public UserServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByUsersId() {
        return roleRepository.findByUsersId(SecurityUtils.getCurrentUserId());
    }
}
