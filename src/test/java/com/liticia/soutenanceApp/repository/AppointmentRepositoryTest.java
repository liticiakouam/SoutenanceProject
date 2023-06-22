package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Appointment;
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
public class AppointmentRepositoryTest {
    @Autowired
    private AppointmentRepository appointmentRepository;


    @Test
    void testShouldFindAppointmentByAvailabityId() {
        List<Appointment> appointments = appointmentRepository.findAppointmentByAvailabilityId(1L);
        assertEquals(0, appointments.size());
    }
}
