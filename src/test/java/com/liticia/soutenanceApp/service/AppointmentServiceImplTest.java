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
import org.springframework.data.util.Pair;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
