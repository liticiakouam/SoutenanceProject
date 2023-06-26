package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/soutenanceProTest?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class AppointmentRepositoryTest {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testShouldFindAppointmentByAvailabilityId() {
        List<Appointment> appointments = appointmentRepository.findAppointmentByAvailabilityId(1L);
        assertEquals(0, appointments.size());
    }

    @Test
    void testShouldFindByUserCustomerAndCreatedAt() {
        Instant date = Instant.now();
        User user = User.builder().id(2).build();
        appointmentRepository.findByUserCustomerAndCreatedAt(user, date);
    }

    @Test
    void testShouldFindById() {
        Optional<Appointment> appointment = appointmentRepository.findById(1L);
        assertTrue(appointment.isEmpty());
    }

    @Test
    void findAllByUserCustomerAndReport() {
        List<Appointment> appointments = appointmentRepository.findIncompletedAppointementByDate(LocalDate.now(), SecurityUtils.getCurrentUserId());

        assertEquals(0, appointments.size());
    }

    @Test
    void findPageableByUserCustomerAndReport() {
        Optional<User> user = userRepository.findById(SecurityUtils.getCurrentUserId());
        Pageable pageable = PageRequest.of(1, 2);
        Page<Appointment> appointmentPage = appointmentRepository.findAllByUserCustomerAndReportOrderByCreatedAtDesc(user.get(), null, pageable);

        assertEquals(0, appointmentPage.getTotalPages());
        assertEquals(0, appointmentPage.getTotalElements());
    }

    @Test
    void testShouldFindAppointmentByOldDate() {
        LocalDate now = LocalDate.now();
        List<Appointment> appointment = appointmentRepository.findOldAppointmentByDate(now, SecurityUtils.getCurrentUserId());
        assertTrue(appointment.isEmpty());
    }
}
