package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.exception.UserNotFoundException;
import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.repository.AppointmentRepository;
import com.liticia.soutenanceApp.repository.AvailabilityRepository;
import com.liticia.soutenanceApp.repository.ProfessionnalRepository;
import com.liticia.soutenanceApp.repository.RoleRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.serviceImpl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceImplTest {
    private final ProfessionnalRepository professionnalRepository = Mockito.mock(ProfessionnalRepository.class);
    private final AvailabilityRepository availabilityRepository = Mockito.mock(AvailabilityRepository.class);
    private final AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
    private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);

    private final AppointmentService appointmentService = new AppointmentServiceImpl(appointmentRepository, professionnalRepository, availabilityRepository, roleRepository);


    @Test
    void testShouldSaveAppointment() throws ParseException, IOException {
        User user = User.builder().id(1).build();
        User userPro = User.builder().id(2).build();
        Availability availability = Availability.builder().id(1).user(User.builder().id(2).build()).build();
        Appointment appointment = Appointment.builder().id(4).build();
        AppointmentCreate appointmentCreate = AppointmentCreate.builder().pattern("hello").build();
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());

        when(professionnalRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(professionnalRepository.findById(2L)).thenReturn(Optional.of(userPro));
        when(availabilityRepository.findById(1L)).thenReturn(Optional.ofNullable(availability));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        appointmentService.save(appointmentCreate, file, "image", 1L);

        verify(professionnalRepository, times(1)).findById(SecurityUtils.getCurrentUserId());
        verify(professionnalRepository, times(1)).findById(2L);
        verify(availabilityRepository, times(1)).findById(1L);
    }

   @Test
    void testShouldThrowExceptionWhenUnSaveAppointment() throws ParseException, IOException {
        Availability availability = Availability.builder().id(1).user(User.builder().id(2).build()).build();
        Appointment appointment = Appointment.builder().id(4).build();
        AppointmentCreate appointmentCreate = AppointmentCreate.builder().pattern("hello").build();
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());

        when(availabilityRepository.findById(1L)).thenReturn(Optional.ofNullable(availability));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        assertThrows(UserNotFoundException.class, ()->appointmentService.save(appointmentCreate, file, "image", 1L));

        verify(professionnalRepository, times(1)).findById(SecurityUtils.getCurrentUserId());
        verify(professionnalRepository, times(1)).findById(2L);
        verify(availabilityRepository, times(1)).findById(1L);
    }

    @Test
    void testShouldFindAppointmentByUserCustomerAndCreatedAt() {
        Instant date = Instant.now();
        User user = User.builder().id(2).build();
        Appointment optionalAppointment = Appointment.builder().id(1).build();
        when(professionnalRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(appointmentRepository.findByUserCustomerAndCreatedAt(user, date)).thenReturn(Optional.of(optionalAppointment));

       appointmentService.findByUserCustomerAndCreatedAt();
    }

    @Test
    void testShouldThrowExceptionWhenEmptyUserCustomerAndCreatedAt() {
        Instant date = Instant.now();
        User user = User.builder().id(2).build();
        Appointment optionalAppointment = Appointment.builder().id(1).build();
        when(appointmentRepository.findByUserCustomerAndCreatedAt(user, date)).thenReturn(Optional.of(optionalAppointment));

        assertThrows(UserNotFoundException.class, appointmentService::findByUserCustomerAndCreatedAt);
    }

    @Test
    void testShouldFindIncompletedAppointmentByUserCustomer() {
        User user = User.builder().id(1).build();
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).document("pdf").build(),
                Appointment.builder().id(2).build(),
                Appointment.builder().id(3).build()
        );
        Role role = Role.builder().id(2).name("client").build();
        when(appointmentRepository.findUserCustomerIncompletedAppointementByDate(LocalDate.now(), SecurityUtils.getCurrentUserId())).thenReturn(list);
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));

        List<Appointment> appointments = appointmentService.findAllByReportAndUser();
        assertEquals(3, appointments.size());
        assertEquals(2, appointments.get(1).getId());
    }

    @Test
    void testShouldFindIncompletedAppointmentByUserCustomerReturnNull() {
        List<Appointment> list = List.of();
        Role role = Role.builder().id(2).name("client").build();
        when(appointmentRepository.findUserCustomerIncompletedAppointementByDate(LocalDate.now(), SecurityUtils.getCurrentUserId())).thenReturn(list);
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));

        List<Appointment> appointments = appointmentService.findAllByReportAndUser();
        assertEquals(0, appointments.size());
    }
    @Test
    void testShouldFindIncompletedAppointmentByUserPro() {
        User user = User.builder().id(1).build();
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).document("pdf").build(),
                Appointment.builder().id(2).build(),
                Appointment.builder().id(3).build()
        );
        Role role = Role.builder().id(3).name("client").build();
        when(appointmentRepository.findUserProIncompletedAppointementByDate(LocalDate.now(), SecurityUtils.getCurrentUserId())).thenReturn(list);
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));

        List<Appointment> appointments = appointmentService.findAllByReportAndUser();
        assertEquals(3, appointments.size());
        assertEquals(2, appointments.get(1).getId());
    }

    @Test
    void testShouldFindIncompletedAppointmentByUserProReturnNull() {
        List<Appointment> list = List.of();
        Role role = Role.builder().id(3).name("client").build();
        when(appointmentRepository.findUserCustomerIncompletedAppointementByDate(LocalDate.now(), SecurityUtils.getCurrentUserId())).thenReturn(list);
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));

        List<Appointment> appointments = appointmentService.findAllByReportAndUser();
        assertEquals(0, appointments.size());
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
        when(professionnalRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(appointmentRepository.findAllByUserCustomerAndReportOrderByCreatedAtDesc(user, null, pageable)).thenReturn(page);

        Page<Appointment> appointmentPage = appointmentService.findPageByReportAndUser(pageable);
        assertEquals(3, appointmentPage.getTotalElements());
        assertEquals(1, appointmentPage.getTotalPages());
    }

    @Test
    void testShouldFindAppointmentById() {
        User user = User.builder().id(1).build();
        Appointment appointmentBuild = Appointment.builder().id(1).document("pdf").build();

        when(professionnalRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentBuild));

        Optional<Appointment> appointment = appointmentService.findById(1L);
        assertTrue(appointment.isPresent());
    }

    @Test
    void testShouldFindOldAppointmentByUserCustomer() {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).report(Report.builder().note("fe").build()).document("pdf").build(),
                Appointment.builder().id(2).report(Report.builder().note("fe").build()).build(),
                Appointment.builder().id(3).report(Report.builder().note("fe").build()).build()
        );
        LocalDate now = LocalDate.now();
        Role role = Role.builder().id(2).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserCustomerOldAppointmentByDate(now, 5L)).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentByOldDate();
        assertEquals(3, appointments.size());
    }

    @Test
    void testShouldFindOldAppointmentByUserCustomerAndReturnNull() {
        List<Appointment> list = List.of();
        LocalDate now = LocalDate.now();
        Role role = Role.builder().id(2).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserCustomerOldAppointmentByDate(now, 5L)).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentByOldDate();
        assertEquals(0, appointments.size());
    }

    @Test
    void testShouldFindOldAppointmentByUserPro() {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).report(Report.builder().note("fe").build()).document("pdf").build(),
                Appointment.builder().id(2).report(Report.builder().note("fe").build()).build(),
                Appointment.builder().id(3).report(Report.builder().note("fe").build()).build()
        );
        LocalDate now = LocalDate.now();
        Role role = Role.builder().id(3).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserProOldAppointmentByDate(now, 5L)).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentByOldDate();
        assertEquals(3, appointments.size());
    }

    @Test
    void testShouldFindOldAppointmentByUserProAndReturnNull() {
        List<Appointment> list = List.of();
        LocalDate now = LocalDate.now();
        Role role = Role.builder().id(3).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserProOldAppointmentByDate(now, 5L)).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentByOldDate();
        assertEquals(0, appointments.size());
    }

    @Test
    void testShouldFindAppointmentToComeByUserCustomer() {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).report(Report.builder().note("fe").build()).document("pdf").build(),
                Appointment.builder().id(2).report(Report.builder().note("fe").build()).build(),
                Appointment.builder().id(3).report(Report.builder().note("fe").build()).build()
        );
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        Role role = Role.builder().id(2).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserCustomerAppointmentToComeByDate(localDate, SecurityUtils.getCurrentUserId())).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        assertEquals(3, appointments.size());
    }

    @Test
    void testShouldFindAppointmentToComeByUserCustomerReturnNull() {
        List<Appointment> list = List.of();
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        Role role = Role.builder().id(2).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserCustomerAppointmentToComeByDate(localDate, SecurityUtils.getCurrentUserId())).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        assertEquals(0, appointments.size());
    }

    @Test
    void testShouldFindAppointmentToComeByUserPro() {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).report(Report.builder().note("fe").build()).document("pdf").build(),
                Appointment.builder().id(2).report(Report.builder().note("fe").build()).build(),
                Appointment.builder().id(3).report(Report.builder().note("fe").build()).build()
        );
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        Role role = Role.builder().id(3).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserProAppointmentToComeByDate(localDate, SecurityUtils.getCurrentUserId())).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        assertEquals(3, appointments.size());
    }

    @Test
    void testShouldFindAppointmentToComeByUserProReturnNull() {
        List<Appointment> list = List.of();
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        Role role = Role.builder().id(3).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserProAppointmentToComeByDate(localDate, SecurityUtils.getCurrentUserId())).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        assertEquals(0, appointments.size());
    }

    @Test
    void testShouldFindRecentAppointmentByUserPro() {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).report(Report.builder().note("fe").build()).document("pdf").build(),
                Appointment.builder().id(2).report(Report.builder().note("fe").build()).build(),
                Appointment.builder().id(3).report(Report.builder().note("fe").build()).build()
        );
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        Role role = Role.builder().id(2).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserProRecentAppointmentByDate(now, localDate, SecurityUtils.getCurrentUserId())).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        assertEquals(0, appointments.size());
    }

    @Test
    void testShouldFindRecentAppointmentByUserProReturnNull() {
        List<Appointment> list = List.of();
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        Role role = Role.builder().id(3).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserProRecentAppointmentByDate(now, localDate, SecurityUtils.getCurrentUserId())).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        assertEquals(0, appointments.size());
    }

    @Test
    void testShouldFindRecentAppointmentByUserCustomer() {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).report(Report.builder().note("fe").build()).document("pdf").build(),
                Appointment.builder().id(2).report(Report.builder().note("fe").build()).build(),
                Appointment.builder().id(3).report(Report.builder().note("fe").build()).build()
        );
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        Role role = Role.builder().id(2).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserCustomerRecentAppointmentByDate(now, localDate, SecurityUtils.getCurrentUserId())).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        assertEquals(0, appointments.size());
    }

    @Test
    void testShouldFindRecentAppointmentByUserCustomerReturnNull() {
        List<Appointment> list = List.of();
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(2);
        Role role = Role.builder().id(2).name("client").build();
        when(roleRepository.findByUsersId(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(role));
        when(appointmentRepository.findUserCustomerRecentAppointmentByDate(now, localDate, SecurityUtils.getCurrentUserId())).thenReturn(list);

        List<Appointment> appointments = appointmentService.findAppointmentToComeByDate();
        assertEquals(0, appointments.size());
    }

    @Test
    void testShouldDeleteAppointment() {
        Appointment appointmentBuild = Appointment.builder().id(1).document("pdf").build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentBuild));
        when(appointmentRepository.save(appointmentBuild)).thenReturn(appointmentBuild);

        appointmentService.deleteAppointment(1);
    }

}
