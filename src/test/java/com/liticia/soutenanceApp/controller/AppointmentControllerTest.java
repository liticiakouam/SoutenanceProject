package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.model.Schedule;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.AppointmentService;
import com.liticia.soutenanceApp.service.AvailabilityService;
import com.liticia.soutenanceApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = {AppointmentController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AppointmentService appointmentService;

    @Test
    public void testShouldSaveAppointment() throws Exception {
        AppointmentCreate appointmentCreate = AppointmentCreate.builder().pattern("hello").build();
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());

        doNothing().when(appointmentService).save(appointmentCreate, file, "image", 1L);
        mockMvc.perform(multipart("/appointment/add")
                .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("confirmrdv"))
                .andReturn();
    }

}
