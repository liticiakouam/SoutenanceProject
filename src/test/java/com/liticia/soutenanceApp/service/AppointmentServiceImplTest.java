package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.Schedule;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.AppointmentRepository;
import com.liticia.soutenanceApp.repository.AvailabilityRepository;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.serviceImpl.AppointmentServiceImpl;
import com.liticia.soutenanceApp.service.serviceImpl.AvailabilityServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AppointmentServiceImplTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final AvailabilityRepository availabilityRepository = Mockito.mock(AvailabilityRepository.class);
    private final AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);

    private final AppointmentService appointmentService = new AppointmentServiceImpl(appointmentRepository, userRepository, availabilityRepository);


    @Test
    void testShouldSaveAppointment() throws ParseException, IOException {
        User user = User.builder().id(1).build();
        User userPro = User.builder().id(2).build();
        Availability availability = Availability.builder().id(1).user(User.builder().id(2).build()).build();
        Appointment appointment = Appointment.builder().id(4).build();
        AppointmentCreate appointmentCreate = AppointmentCreate.builder().pattern("hello").build();
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());

        when(userRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userPro));
        when(availabilityRepository.findById(1L)).thenReturn(Optional.ofNullable(availability));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        appointmentService.save(appointmentCreate, file, "image", 1L);

        verify(userRepository, times(2)).findById(SecurityUtils.getCurrentUserId());
        verify(userRepository, times(2)).findById(2L);
        verify(availabilityRepository, times(1)).findById(1L);
    }

    @Test
    void testShouldFindAppointmentByUserCustomerAndCreatedAt() {
        Instant date = Instant.now();
        User user = User.builder().id(2).build();
        Appointment optionalAppointment = Appointment.builder().id(1).build();
        when(userRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(appointmentRepository.findByUserCustomerAndCreatedAt(user, date)).thenReturn(Optional.of(optionalAppointment));

       appointmentService.findByUserCustomerAndCreatedAt();
    }

    @Test
    void testShouldFindAppointmentByReportAndUser() {
        User user = User.builder().id(1).build();
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).document("pdf").build(),
                Appointment.builder().id(2).build(),
                Appointment.builder().id(3).build()
        );
        when(userRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(appointmentRepository.findAllByUserCustomerAndReport(user, null)).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAllByReportAndUser();
        assertEquals(3, appointments.size());
        assertEquals(2, appointments.get(1).getId());
    }

    @Test
    void testShouldFindAppointmentByReportAndUserAndPageable() {
        User user = User.builder().id(1).build();
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).document("pdf").build(),
                Appointment.builder().id(2).build(),
                Appointment.builder().id(3).build()
        );
        Pageable pageable = PageRequest.of(1, 2);
        Page<Appointment> page = new PageImpl<>(list);
        when(userRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(appointmentRepository.findAllByUserCustomerAndReportOrderByCreatedAtDesc(user, null, pageable)).thenReturn(page);

        Page<Appointment> appointmentPage = appointmentService.findPageByReportAndUser(pageable);
        assertEquals(3, appointmentPage.getTotalElements());
        assertEquals(1, appointmentPage.getTotalPages());
    }

    @Test
    void testShouldFindAppointmentById() {
        User user = User.builder().id(1).build();
        Appointment appointmentBuild = Appointment.builder().id(1).document("pdf").build();

        when(userRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentBuild));

        Optional<Appointment> appointment = appointmentService.findById(1L);
        assertTrue(appointment.isPresent());
    }

}
