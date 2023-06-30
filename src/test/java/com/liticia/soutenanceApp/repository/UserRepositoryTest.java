package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Role;
import com.liticia.soutenanceApp.model.User;
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
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testShouldReturnUserId() {
        Optional<User> optionalUser = userRepository.findById(1L);
        assertTrue(optionalUser.isPresent());
        assertEquals(1, optionalUser.get().getId());
    }

    @Test
    void testShouldReturnCityNameByUserId() {
        Optional<User> optionalUser = userRepository.findById(2L);
        assertTrue(optionalUser.isPresent());
        assertEquals("Douala", optionalUser.get().getCity().getName());
    }

    @Test
    void testShouldReturnUsers() {
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertEquals(5, users.size());
    }

}
