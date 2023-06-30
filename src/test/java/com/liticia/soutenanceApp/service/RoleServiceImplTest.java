package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.model.Role;
import com.liticia.soutenanceApp.repository.RoleRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.serviceImpl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RoleServiceImplTest {
    private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);

    private final RoleService cityService = new RoleServiceImpl(roleRepository);


    @Test
    void testShouldFindCities() {
        Role role = Role.builder().id(1).name("client").build();

        when(roleRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        Optional<Role> optionalRole = cityService.findByUsersId();
        assertTrue(optionalRole.isEmpty());
    }

}
