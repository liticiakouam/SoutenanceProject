package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/soutenanceProTest?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class ProfessionnalRepositoryTest {
    @Autowired
    private ProfessionnalRepository professionnalRepository;

    @Test
    void testShouldSearchUsers() {
        List<User> searchResult = professionnalRepository.searchUsers("douala",  "informatique", "liti");

        assertEquals(5, searchResult.size());
    }

    @Test
    void testShouldFindUsers() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<User> users = professionnalRepository.findAllByRolesIdOrderByCreatedAtDesc(pageable,2);

        assertEquals(1, users.getTotalPages());
    }

    @Test
    void testShouldFindUserById() {
        Optional<User> user = professionnalRepository.findById(3L);
        assertTrue(user.isPresent());
    }
}
