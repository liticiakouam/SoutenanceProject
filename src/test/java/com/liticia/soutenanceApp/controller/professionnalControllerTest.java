package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.*;
import com.liticia.soutenanceApp.utils.StartDayOfWeek;
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
import java.util.Set;

import static com.liticia.soutenanceApp.utils.StartDayOfWeek.getStartOfWeekDay;
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


    @MockBean
    private RoleService roleService;

    @MockBean
    private UserService userService;

    @Test
    public void testShouldVerifyThatControllerReturnSearchUserResult() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().firstName("liti").lastName("anzwe").speciality(Speciality.builder().id(1L).name("informatique").build()).city(City.builder().id(1L).name("doualq").build()).build(),
                User.builder().firstName("momo").build()
        );
        User user = User.builder().id(1).speciality(Speciality.builder().name("info").build()).city(City.builder().name("buea").build()).build();
        when(userService.findById()).thenReturn(Optional.of(user));
        when(professionnalService.searchUser("douala","informatique","liti")).thenReturn(users);

        mockMvc.perform(get("/user/search?keyword=liti&city=1&speciality=1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users?pageNumber=1"))
                .andReturn();
    }

    @Test
    public void testShouldReturnErrorPageWhenUserNotFound() throws Exception {
        User user = User.builder().id(1).speciality(Speciality.builder().name("info").build()).city(City.builder().name("buea").build()).build();
        LocalDate startDate = LocalDate.now();
        startDate = getStartOfWeekDay(startDate);
        AvailabilityResponse availabilityResponse = AvailabilityResponse.builder().nextStartDate(startDate.plusWeeks(1)).previousStartDate(startDate.minusWeeks(1)).build();

        when(professionnalService.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(availabilityService.getAvailabilities(startDate, user)).thenReturn(availabilityResponse);
        mockMvc.perform(get("/user?userId=1&startDate=2023-06-20"))
                .andExpect(status().isOk())
                .andExpect(view().name("serverError"))
                .andReturn();
    }
}
