package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.Schedule;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.AvailabilityRepository;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.serviceImpl.AvailabilityServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.util.Pair;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AvailabilityServiceImplTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final AvailabilityRepository availabilityRepository = Mockito.mock(AvailabilityRepository.class);

    private final AvailabilityService availabilityService = new AvailabilityServiceImpl(availabilityRepository, userRepository);


    @Test
    void testShouldSaveAvailabilities() throws ParseException {
        User user = User.builder().id(1).build();
        List<Availability> availabilities = Arrays.asList(
                Availability.builder().id(1).user(user).build(),
                Availability.builder().id(2).user(user).build()
        );
        AvailabilityCreate availabilityCreate= AvailabilityCreate.builder().schedule(Schedule.AFTERNOON).build();

        when(userRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(availabilityRepository.saveAll(availabilities)).thenReturn(availabilities);

        availabilityService.saveAvailabilities(availabilityCreate);

        verify(userRepository, times(1)).findById(SecurityUtils.getCurrentUserId());
    }

    @Test
    void testShouldGetAvailability() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);
        User user = User.builder().id(1).build();
        List<Availability> list = Arrays.asList(
                Availability.builder().id(1).user(user).build(),
                Availability.builder().id(2).user(user).build()
        );
        when(availabilityRepository.findAllByUserAndDateBetweenOrderByDate(user, startDate, endDate)).thenReturn(list);

        AvailabilityResponse availabilities = availabilityService.getAvailabilities(startDate, user);
        assertEquals(7, availabilities.getAvailabilities().length);
    }

    @Test
    void testShouldGenerateTime() {
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        List<Pair<LocalTime, LocalTime>> times = availabilityService.generateTimes(startTime, endTime);
        assertEquals(4, times.size());
    }

}
