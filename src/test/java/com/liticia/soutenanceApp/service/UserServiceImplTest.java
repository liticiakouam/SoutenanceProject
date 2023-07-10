package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.*;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CityRepository cityRepository = Mockito.mock(CityRepository.class);
    private final SpecialityRepository specialityRepository = Mockito.mock(SpecialityRepository.class);
    private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);

    private final DemandRepository demandRepository = Mockito.mock(DemandRepository.class);

    private final UserService userService = new UserServiceImpl(userRepository, cityRepository, specialityRepository, roleRepository, demandRepository, passwordEncoder);


    @Test
    void testShouldFindCities() {
        User user = User.builder().id(1).build();

        when(userRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findById();
        assertTrue(optionalUser.isPresent());
    }

}
