package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.exception.UserNotFoundException;
import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.Schedule;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.AvailabilityRepository;
import com.liticia.soutenanceApp.repository.ProfessionnalRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AvailabilityServiceImplTest {
    private final ProfessionnalRepository professionnalRepository = Mockito.mock(ProfessionnalRepository.class);
    private final AvailabilityRepository availabilityRepository = Mockito.mock(AvailabilityRepository.class);

    private final AvailabilityService availabilityService = new AvailabilityServiceImpl(availabilityRepository, professionnalRepository);

    @Test
    void testShouldSaveAvailabilities() throws ParseException {
        User user = User.builder().id(1).build();
        List<Availability> availabilities = Arrays.asList(
                Availability.builder().id(1).user(user).build(),
                Availability.builder().id(2).user(user).build()
        );
        AvailabilityCreate availabilityCreate= AvailabilityCreate.builder().schedule(Schedule.AFTERNOON).build();

        when(professionnalRepository.findById(SecurityUtils.getCurrentUserId())).thenReturn(Optional.of(user));
        when(availabilityRepository.saveAll(availabilities)).thenReturn(availabilities);

        availabilityService.saveAvailabilities(availabilityCreate);

        verify(professionnalRepository, times(1)).findById(SecurityUtils.getCurrentUserId());
    }

    @Test
    void testShouldThrowExceptionWhenUnSaveAvailabilities() throws ParseException {
        User user = User.builder().id(1).build();
        List<Availability> availabilities = Arrays.asList(
                Availability.builder().id(1).user(user).build(),
                Availability.builder().id(2).user(user).build()
        );
        AvailabilityCreate availabilityCreate= AvailabilityCreate.builder().schedule(Schedule.AFTERNOON).build();

        when(availabilityRepository.saveAll(availabilities)).thenReturn(availabilities);

        assertThrows(UserNotFoundException.class, ()->availabilityService.saveAvailabilities(availabilityCreate));
        verify(professionnalRepository, times(1)).findById(SecurityUtils.getCurrentUserId());
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
    void testShouldGenerateTimeFirstPeriod() {
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        List<Pair<LocalTime, LocalTime>> times = availabilityService.generateTimes(startTime, endTime);
        assertEquals(4, times.size());
    }

    @Test
    void testShouldGenerateTimeSecondPeriod() {
        LocalTime startTime = LocalTime.of(13, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        List<Pair<LocalTime, LocalTime>> times = availabilityService.generateTimes(startTime, endTime);
        assertEquals(4, times.size());
    }

    @Test
    void testShouldGenerateFirstAndEndTimeForMorningPeriod() {
        Pair<LocalTime, LocalTime> localTimePair = availabilityService.getSchedule(Schedule.MORNING);
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        assertEquals(startTime, localTimePair.getFirst());
        assertEquals(endTime, localTimePair.getSecond());
    }

    @Test
    void testShouldGenerateFirstAndEndTimeForFulldayPeriod() {
        Pair<LocalTime, LocalTime> localTimePair = availabilityService.getSchedule(Schedule.FULLDAY);
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        assertEquals(startTime, localTimePair.getFirst());
        assertEquals(endTime, localTimePair.getSecond());
    }

    @Test
    void testShouldFindAvailabilityId() {
        Availability availabilityBuil = Availability.builder().id(1).build();
        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availabilityBuil));

        Optional<Availability> availability = availabilityService.findById(1L);
        assertEquals(1, availability.get().getId());
    }

}
