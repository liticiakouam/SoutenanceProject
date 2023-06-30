package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/soutenanceProTest?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class CityRepositoryTest {
    @Autowired
    private CityRepository cityRepository;

    @Test
    void testShouldReturnCities() {
        List<City> cities = cityRepository.findAll();
        assertEquals(1, cities.size());
    }

}
