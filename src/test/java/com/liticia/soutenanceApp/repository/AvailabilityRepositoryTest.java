package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/soutenanceProTest?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class AvailabilityRepositoryTest {
    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Test
    void testShouldReturnAvailabilities() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);
        User user = User.builder().id(1).build();
        List<Availability> availabilities = availabilityRepository.findAllByUserAndDateBetweenOrderByDate(user, startDate, endDate);

        assertEquals(1, availabilities.size());
    }

    @Test
    void testShouldReturnNullAvailabilities() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);
        User user = User.builder().id(100).build();
        List<Availability> availabilities = availabilityRepository.findAllByUserAndDateBetweenOrderByDate(user, startDate, endDate);

        assertEquals(0, availabilities.size());
    }
}
