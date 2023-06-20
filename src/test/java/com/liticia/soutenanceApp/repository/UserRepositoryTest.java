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
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testShouldSearchUsers() {
        List<User> searchResult = userRepository.searchUsers("douala",  "informatique", "liti");

        assertEquals(5, searchResult.size());
        assertEquals("abena@gmail.com", searchResult.get(0).getEmail());
    }

    @Test
    void testShouldFindUsers() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<User> users = userRepository.findAllByRolesIdOrderByCreatedAtDesc(pageable,3);

        assertEquals(3, users.getTotalPages());
        assertEquals(5, users.getTotalElements());
    }

    @Test
    void testShouldFindUserById() {
        Optional<User> user = userRepository.findById(3L);
        assertTrue(user.isPresent());
        assertEquals(3, user.get().getId());
        assertEquals("Douala", user.get().getCity().getName());
    }
}
