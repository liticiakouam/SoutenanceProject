package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.config.TestConfig;
import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.repository.*;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.AvailabilityService;
import com.liticia.soutenanceApp.service.ProfessionnalService;
import com.liticia.soutenanceApp.service.RoleService;
import com.liticia.soutenanceApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AvailabiltyController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(TestConfig.class)
public class AvailabilityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessionnalService professionnalService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityUtils securityUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AvailabilityService availabilityService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private AvailabilityRepository availabilityRepository;
    @MockBean
    private AppointmentRepository appointmentRepository;

    @Test
    public void testShouldSaveAvailability() throws Exception {
        AvailabilityCreate availabilityCreate= AvailabilityCreate.builder().schedule(Schedule.AFTERNOON).build();

        doNothing().when(availabilityService).saveAvailabilities(availabilityCreate);
        mockMvc.perform(post("/professional/availability/add"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/professional/availability?startDate=2023-01-01"))
                .andReturn();
    }

    public void testShouldRetrieveAvailabilities() throws Exception {
        User user = User.builder().id(1).build();
        LocalDate startDate = LocalDate.now();
        long[][] availabilities = new long[7][9];
        AvailabilityResponse availabilityResponse = AvailabilityResponse.builder().availabilities(availabilities).nextStartDate(startDate.plusWeeks(1)).previousStartDate(startDate.minusWeeks(1)).build();

        when(professionnalService.findById(securityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(availabilityService.getAvailabilities(startDate, user)).thenReturn(availabilityResponse);
        mockMvc.perform(get("/professional/availability?startDate=2023-01-01"))
                .andExpect(status().isOk())
                .andExpect(view().name("availability"))
                .andReturn();
    }

    @Test
    public void testShouldGetAvailabilityId() throws Exception {
        Availability availability = Availability.builder().id(1).user(User.builder().speciality("info").build()).build();
        User user = User.builder().id(1).speciality("info").city("buea").build();
        Role role = Role.builder().id(2).build();
        when(userService.findById()).thenReturn(Optional.of(user));
        when(roleService.findByUsersId()).thenReturn(Optional.of(role));
        when(availabilityService.findById(1L)).thenReturn(Optional.ofNullable(availability));
        mockMvc.perform(get("/availabilityId?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("motifrdv"))
                .andReturn();
    }
}
