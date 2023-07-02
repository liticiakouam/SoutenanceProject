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
    private ProfessionnalRepository professionnalRepository;

    @Test
    void testShouldFindAppointmentByAvailabilityIdReturnNull() {
        List<Appointment> appointments = appointmentRepository.findAppointmentByAvailabilityId(1L);
        assertEquals(0, appointments.size());
    }
    @Test
    void testShouldFindAppointmentByAvailabilityId() {
        List<Appointment> appointments = appointmentRepository.findAppointmentByAvailabilityId(73L);
        assertEquals(1, appointments.size());
    }

    @Test
    void testShouldFindByUserCustomerAndCreatedAt() {
        Instant date = Instant.now();
        User user = User.builder().id(2).build();
        Optional<Appointment> optionalAppointment = appointmentRepository.findByUserCustomerAndCreatedAt(user, date);

        assertTrue(optionalAppointment.isEmpty());
    }

    @Test
    void testShouldFindByIdAndReturnNull() {
        Optional<Appointment> appointment = appointmentRepository.findById(1L);
        assertTrue(appointment.isEmpty());
    }

    @Test
    void testShouldFindById() {
        Optional<Appointment> appointment = appointmentRepository.findById(115L);
        assertTrue(appointment.isPresent());

        assertEquals(115, appointment.get().getId());
    }

    @Test
    void findAllByUserCustomerIncompletedAppointment() {
        List<Appointment> appointments = appointmentRepository.findUserCustomerIncompletedAppointmentByDate(LocalDate.now(), SecurityUtils.getCurrentUserId());

        assertEquals(3, appointments.size());
    }

    @Test
    void findAllByUserCustomerIncompletedAppointmentReturnNull() {
        List<Appointment> appointments = appointmentRepository.findUserCustomerIncompletedAppointmentByDate(LocalDate.now(), 1L);

        assertEquals(0, appointments.size());
    }


    @Test
    void findAllByUserProIncompletedAppointmentReturnNull() {
        List<Appointment> appointments = appointmentRepository.findUserProIncompletedAppointmentByDate(LocalDate.now(), SecurityUtils.getCurrentUserId());

        assertEquals(0, appointments.size());
    }

    @Test
    void findAllByUserProIncompletedAppointment() {
        List<Appointment> appointments = appointmentRepository.findUserProIncompletedAppointmentByDate(LocalDate.now(), 1L);

        assertEquals(3, appointments.size());
    }

    @Test
    void findPageableByUserCustomerAndReport() {
        Optional<User> user = professionnalRepository.findById(SecurityUtils.getCurrentUserId());
        Pageable pageable = PageRequest.of(1, 2);
        Page<Appointment> appointmentPage = appointmentRepository.findAllByUserCustomerAndReportOrderByCreatedAtDesc(user.get(), null, pageable);

        assertEquals(3, appointmentPage.getTotalPages());
        assertEquals(5, appointmentPage.getTotalElements());
    }

    @Test
    void testShouldFindUserCustomerAppointmentByOldDate() {
        LocalDate now = LocalDate.now();
        List<Appointment> appointment = appointmentRepository.findUserCustomerOldAppointmentByDate(now, 5L);
        assertFalse(appointment.isEmpty());
        assertEquals(1, appointment.size());
    }

    @Test
    void testShouldFindUserCustomerAppointmentByOldDateReturnNull() {
        LocalDate now = LocalDate.now();
        List<Appointment> appointment = appointmentRepository.findUserCustomerOldAppointmentByDate(now, 1L);
        assertTrue(appointment.isEmpty());
    }

    @Test
    void testShouldFindUserProAppointmentByOldDate() {
        LocalDate now = LocalDate.now();
        List<Appointment> appointment = appointmentRepository.findUserProOldAppointmentByDate(now, 1L);
        assertFalse(appointment.isEmpty());
        assertEquals(1, appointment.size());
    }

    @Test
    void testShouldFindUserProAppointmentByOldDateReturnNull() {
        LocalDate now = LocalDate.now();
        List<Appointment> appointment = appointmentRepository.findUserProOldAppointmentByDate(now, 5L);
        assertTrue(appointment.isEmpty());
    }

    @Test
    void testShouldFindUserCustomerBeComingAppointment() {
        LocalDate now = LocalDate.now().plusDays(2);
        List<Appointment> appointment = appointmentRepository.findUserCustomerAppointmentToComeByDate(now, 5L);
        assertTrue(appointment.isEmpty());
    }

    @Test
    void testShouldFindUserCustomerBeComingAppointmentAndReturnNull() {
        LocalDate now = LocalDate.now().plusDays(2);
        List<Appointment> appointment = appointmentRepository.findUserCustomerAppointmentToComeByDate(now, 7L);
        assertTrue(appointment.isEmpty());
    }

    @Test
    void testShouldFindUserProBeComingAppointment() {
        LocalDate now = LocalDate.now().plusDays(2);
        List<Appointment> appointment = appointmentRepository.findUserProAppointmentToComeByDate(now, 1L);
        assertTrue(appointment.isEmpty());
    }

    @Test
    void testShouldFindUserProBeComingAppointmentAndReturnNull() {
        LocalDate now = LocalDate.now().plusDays(2);
        List<Appointment> appointment = appointmentRepository.findUserProAppointmentToComeByDate(now, 7L);
        assertTrue(appointment.isEmpty());
    }

    @Test
    void testShouldFindUserCustomerRecentAppointment() {
        LocalDate now = LocalDate.now().plusDays(2);
        List<Appointment> appointment = appointmentRepository.findUserCustomerRecentAppointmentByDate(LocalDate.now(), now,  5L);
        assertFalse(appointment.isEmpty());
        assertEquals(2, appointment.size());
    }

    @Test
    void testShouldFindUserCustomerRecentAppointmentAndReturnNull() {
        LocalDate now = LocalDate.now().plusDays(2);
        List<Appointment> appointment = appointmentRepository.findUserCustomerRecentAppointmentByDate(now, LocalDate.now(), 7L);
        assertTrue(appointment.isEmpty());
    }

    @Test
    void testShouldFindUserProRecentAppointment() {
        LocalDate now = LocalDate.now().plusDays(2);
        List<Appointment> appointment = appointmentRepository.findUserProRecentAppointmentByDate(LocalDate.now(), now,  1L);
        assertFalse(appointment.isEmpty());
        assertEquals(1, appointment.size());
    }

    @Test
    void testShouldFindUserProRecentAppointmentAndReturnNull() {
        LocalDate now = LocalDate.now().plusDays(2);
        List<Appointment> appointment = appointmentRepository.findUserProRecentAppointmentByDate(now, LocalDate.now(), 7L);
        assertTrue(appointment.isEmpty());
    }
}
