package com.liticia.soutenanceApp.controller;

import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.service.CityService;
import com.liticia.soutenanceApp.service.SpecialityService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = {UserController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @MockBean
    private SpecialityService specialityService;

    @MockBean
    private CityService cityService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndUserLengthIsCorrect() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().firstName("liticia").lastName("anzwe").build(),
                User.builder().firstName("momo").build()
        );
        List<Speciality> specialities = Arrays.asList(
                Speciality.builder().name("info").build(),
                Speciality.builder().name("info").build()
        );
        List<City> cities = Arrays.asList(
                City.builder().name("buea").build(),
                City.builder().name("buea").build()
        );
        Pageable pageable = PageRequest.of(1, 2);
        Page<User> page = new PageImpl<>(users);

        when(specialityService.findAll()).thenReturn(specialities);
        when(cityService.findAll()).thenReturn(cities);
        when(userService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get("/users?pageNumber=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("cities"))
                .andExpect(model().attributeExists("specialities"))
                .andReturn();

    }

    @Test
    public void testShouldVerifyThatControllerReturnSearchUserResult() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().firstName("liti").lastName("anzwe").speciality(Speciality.builder().id(1L).name("informatique").build()).city(City.builder().id(1L).name("doualq").build()).build(),
                User.builder().firstName("momo").build()
        );

        when(userService.searchUser("douala","informatique","liti")).thenReturn(users);

        mockMvc.perform(get("/user/search?keyword=liti&city=1&speciality=1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users?pageNumber=1"))
                .andReturn();
    }
}
