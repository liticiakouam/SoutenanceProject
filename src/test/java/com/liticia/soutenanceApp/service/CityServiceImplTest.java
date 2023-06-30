package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.exception.UserNotFoundException;
import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.repository.*;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.serviceImpl.AppointmentServiceImpl;
import com.liticia.soutenanceApp.service.serviceImpl.CityServiceImpl;
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

public class CityServiceImplTest {
    private final CityRepository cityRepository = Mockito.mock(CityRepository.class);

    private final CityService cityService = new CityServiceImpl(cityRepository);


    @Test
    void testShouldFindCities() {
        List<City> list = Arrays.asList(
                City.builder().id(1).build(),
                City.builder().id(2).build(),
                City.builder().id(3).build()
        );

        when(cityRepository.findAll()).thenReturn(list);
        List<City> cities = cityService.findAll();
        assertEquals(3, cities.size());
        assertEquals(2, cities.get(1).getId());
    }

}
