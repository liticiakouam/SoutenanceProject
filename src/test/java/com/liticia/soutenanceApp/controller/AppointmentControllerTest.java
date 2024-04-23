package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.config.TestConfig;
import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.repository.*;
import com.liticia.soutenanceApp.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = {AppointmentController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(TestConfig.class)
public class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private ProfessionnalService professionnalService;

    @MockBean
    private AvailabilityService availabilityService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private AvailabilityRepository availabilityRepository;
    @MockBean
    private AppointmentRepository appointmentRepository;
    @MockBean
    private NotificationService notificationService;

    @Test
    public void testShouldSaveAppointment() throws Exception {
        AppointmentCreate appointmentCreate = AppointmentCreate.builder().pattern("hello").build();
        Appointment appointment = Appointment.builder().pattern("hello").build();
        MockMultipartFile file = new MockMultipartFile("productImage", "filename.txt", "text/plain", "some content".getBytes());

        when(appointmentService.save(appointmentCreate, file, "document", 1L)).thenReturn(appointment);
        mockMvc.perform(multipart("/client/appointment/add")
                .file(file))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testShouldFindAppointmentById() throws Exception {
        Appointment appointment = Appointment.builder().id(1).document("pdf").build();
        User user = User.builder().id(1).speciality("info").city("buea").build();
        Role role = Role.builder().id(2).build();

        when(userService.findById()).thenReturn(Optional.of(user));
        when(roleService.findByUsersId()).thenReturn(Optional.of(role));
        when(appointmentService.findById(1L)).thenReturn(Optional.of(appointment));

        mockMvc.perform(get("/appointment/toComplete"))
                .andExpect(status().isOk())
                .andExpect(view().name("rdv"))
                .andExpect(model().attributeExists("appointments"))
                .andReturn();
    }

    @Test
    void testShouldFindAppointmentByOldDate() throws Exception {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).report(Report.builder().note("fe").build()).userPro(User.builder().firstName("hello").speciality("info").build()).availability(Availability.builder().build()).reportPro(Report.builder().build()).document("pdf").build()
        );
        User user = User.builder().id(1).speciality("info").city("buea").build();
        Role role = Role.builder().id(2).build();

        when(userService.findById()).thenReturn(Optional.of(user));
        when(roleService.findByUsersId()).thenReturn(Optional.of(role));
        when(appointmentService.findAppointmentByOldDate()).thenReturn(list);
        mockMvc.perform(get("/appointment/passer"))
                .andExpect(status().isOk())
                .andExpect(view().name("rdvPasser"))
                .andExpect(model().attributeExists("appointments"))
                .andReturn();
    }

    @Test
    void testShouldFindIncompletedAppointment() throws Exception {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).report(Report.builder().note("fe").build()).userPro(User.builder().firstName("hello").speciality("info").build()).availability(Availability.builder().build()).reportPro(Report.builder().build()).document("pdf").build()
        );
        User user = User.builder().id(1).speciality("info").city("buea").build();
        Role role = Role.builder().id(2).build();

        when(userService.findById()).thenReturn(Optional.of(user));
        when(roleService.findByUsersId()).thenReturn(Optional.of(role));
        when(appointmentService.findAllByReportAndUser()).thenReturn(list);
        mockMvc.perform(get("/appointment/toComplete"))
                .andExpect(status().isOk())
                .andExpect(view().name("rdv"))
                .andExpect(model().attributeExists("appointments"))
                .andReturn();
    }

    @Test
    void testShouldFindAppointmentToComeByDate() throws Exception {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).report(Report.builder().note("fe").build()).userPro(User.builder().firstName("hello").speciality("info").build()).availability(Availability.builder().build()).document("pdf").build()
        );
        User user = User.builder().id(1).speciality("info").city("buea").build();
        Role role = Role.builder().id(2).build();
        when(userService.findById()).thenReturn(Optional.of(user));
        when(roleService.findByUsersId()).thenReturn(Optional.of(role));
        when(appointmentService.findAppointmentToComeByDate()).thenReturn(list);
        mockMvc.perform(get("/appointment/toCome"))
                .andExpect(status().isOk())
                .andExpect(view().name("rdvAvenir"))
                .andExpect(model().attributeExists("appointments"))
                .andReturn();
    }

    @Test
    void testShouldDeleteAppointment() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(1);

        mockMvc.perform(get("/appointment/deleted/1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/appointment/toCome"))
                .andReturn();
    }
}
