package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.dto.ProfessionalCreate;
import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Role;
import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.CityRepository;
import com.liticia.soutenanceApp.repository.RoleRepository;
import com.liticia.soutenanceApp.repository.SpecialityRepository;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final SpecialityRepository specialityRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, CityRepository cityRepository, SpecialityRepository specialityRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.specialityRepository = specialityRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> findById() {
        return userRepository.findById(SecurityUtils.getCurrentUserId());
    }

    @Override
    public List<User> findClients() {
        return userRepository.findByRolesIdOrderByCreatedAtDesc(2);
    }

    @Override
    public List<User> findProfessionals() {
        return userRepository.findByRolesIdOrderByCreatedAtDesc(3);
    }

    @Override
    public void saveProfessional(ProfessionalCreate professionalCreate) {
        City city = cityRepository.findById(professionalCreate.getCityId()).get();
        Speciality speciality = specialityRepository.findById(professionalCreate.getSpecialityId()).get();
        Role role = roleRepository.findById(3L).get();

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        User user = new User();
        user.setEmail(professionalCreate.getEmail());
        user.setFirstName(professionalCreate.getFirstName());
        user.setLastName(professionalCreate.getLastName());
        user.setCity(city);
        user.setSpeciality(speciality);
        user.setRoles(roles);
        user.setCreatedAt(Instant.now());

        userRepository.save(user);
    }
}
