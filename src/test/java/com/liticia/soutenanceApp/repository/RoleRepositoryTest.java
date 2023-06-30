package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/soutenanceProTest?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testShouldReturnRoleId() {
        Optional<Role> optionalRole = roleRepository.findById(1L);
        assertTrue(optionalRole.isPresent());
        assertEquals(1, optionalRole.get().getId());
    }

    @Test
    void testShouldReturnRoleName() {
        Optional<Role> optionalRole = roleRepository.findById(2L);
        assertTrue(optionalRole.isPresent());
        assertEquals("ROLE_CLIENT", optionalRole.get().getName());
    }

    @Test
    void testShouldReturnRoles() {
        List<Role> roles = roleRepository.findAll();
        assertFalse(roles.isEmpty());
        assertEquals("ROLE_CLIENT", roles.get(1).getName());
    }

}
