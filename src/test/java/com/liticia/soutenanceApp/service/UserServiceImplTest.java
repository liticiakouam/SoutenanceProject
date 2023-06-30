package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.model.Role;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.RoleRepository;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.serviceImpl.RoleServiceImpl;
import com.liticia.soutenanceApp.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final UserService userService = new UserServiceImpl(userRepository);


    @Test
    void testShouldFindCities() {
        User user = User.builder().id(1).build();

        when(userRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findById();
        assertTrue(optionalUser.isPresent());
    }

}
