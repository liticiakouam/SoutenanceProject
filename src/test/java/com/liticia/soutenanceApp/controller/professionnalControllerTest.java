package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.AvailabilityService;
import com.liticia.soutenanceApp.service.CityService;
import com.liticia.soutenanceApp.service.ProfessionnalService;
import com.liticia.soutenanceApp.service.SpecialityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ProfessionnalController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class professionnalControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProfessionnalService professionnalService;

    @MockBean
    private SpecialityService specialityService;

    @MockBean
    private CityService cityService;

    @MockBean
    private AvailabilityService availabilityService;

    @Test
    public void testShouldVerifyThatControllerReturnSearchUserResult() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().firstName("liti").lastName("anzwe").speciality(Speciality.builder().id(1L).name("informatique").build()).city(City.builder().id(1L).name("doualq").build()).build(),
                User.builder().firstName("momo").build()
        );

        when(professionnalService.searchUser("douala","informatique","liti")).thenReturn(users);

        mockMvc.perform(get("/user/search?keyword=liti&city=1&speciality=1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users?pageNumber=1"))
                .andReturn();
    }

    @Test
    public void testShouldRetrieveAvailabilities() throws Exception {
        User user = User.builder().id(1).speciality(Speciality.builder().name("info").build()).city(City.builder().name("buea").build()).build();
        LocalDate startDate = LocalDate.now();
        AvailabilityResponse availabilityResponse = AvailabilityResponse.builder().nextStartDate(startDate.plusWeeks(1)).previousStartDate(startDate.minusWeeks(1)).build();

        when(professionnalService.findById(1)).thenReturn(Optional.ofNullable(user));
        when(availabilityService.getAvailabilities(startDate, user)).thenReturn(availabilityResponse);
        mockMvc.perform(get("/user?userId=1&startDate=2023-06-20"))
                .andExpect(status().isOk())
                .andExpect(view().name("agenda"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("nextStartDate"))
                .andExpect(model().attributeExists("previousStartDate"))
                .andReturn();
    }
}
