package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.exception.AppointmenNotFoundException;
import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.AppointmentService;
import com.liticia.soutenanceApp.service.AvailabilityService;
import com.liticia.soutenanceApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
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
public class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private UserService userService;

    @Test
    public void testShouldSaveAppointment() throws Exception {
        AppointmentCreate appointmentCreate = AppointmentCreate.builder().pattern("hello").build();
        MockMultipartFile file = new MockMultipartFile("productImage", "filename.txt", "text/plain", "some content".getBytes());

        doNothing().when(appointmentService).save(appointmentCreate, file, "document", 1L);
        mockMvc.perform(multipart("/appointment/add")
                .file(file))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testShouldFindAppointmentById() throws Exception {
        Appointment appointment = Appointment.builder().id(1).document("pdf").build();

        when(appointmentService.findById(1L)).thenReturn(Optional.of(appointment));

        mockMvc.perform(get("/appointment/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rdv"))
                .andExpect(model().attributeExists("appointment"))
                .andReturn();
    }

    @Test
    void testShouldFindAppointmentByOldDate() throws Exception {
        List<Appointment> list = Arrays.asList(
                Appointment.builder().id(1).document("pdf").build(),
                Appointment.builder().id(2).build(),
                Appointment.builder().id(3).build()
        );
        LocalDate now = LocalDate.now();

        when(appointmentService.findAppointmentByOldDate()).thenReturn(list);

        mockMvc.perform(get("/appointment/passer"))
                .andExpect(status().isOk())
                .andExpect(view().name("rdvPasser"))
                .andExpect(model().attributeExists("appointments"))
                .andReturn();
    }
}
