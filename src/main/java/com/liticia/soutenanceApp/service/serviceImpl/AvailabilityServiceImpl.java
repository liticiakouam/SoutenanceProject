package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.dto.AvailabilityCreate;
import com.liticia.soutenanceApp.dto.AvailabilityResponse;
import com.liticia.soutenanceApp.exception.AvailabilityException;
import com.liticia.soutenanceApp.exception.UnknownScheduleException;
import com.liticia.soutenanceApp.exception.UserNotFoundException;
import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.Schedule;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.AvailabilityRepository;
import com.liticia.soutenanceApp.repository.ProfessionnalRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.AvailabilityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final ProfessionnalRepository professionnalRepository;

    public AvailabilityServiceImpl(AvailabilityRepository availabilityRepository, ProfessionnalRepository professionnalRepository) {
        this.availabilityRepository = availabilityRepository;
        this.professionnalRepository = professionnalRepository;
    }

    @Override
    public void saveAvailabilities(AvailabilityCreate availabilityCreate) {
        Optional<User> optionalUser = professionnalRepository.findById(SecurityUtils.getCurrentUserId());
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        Pair<LocalTime, LocalTime> schedule = getSchedule(availabilityCreate.getSchedule());
        List<Pair<LocalTime, LocalTime>> times = generateTimes(schedule.getFirst(), schedule.getSecond());

        List<Availability> availabilities = new ArrayList<>();

        for (Pair<LocalTime, LocalTime> time : times) {
            Availability availability = new Availability();
            availability.setDate(availabilityCreate.getDate());
            availability.setStartTime(time.getFirst());
            availability.setEndTime(time.getSecond());
            availability.setUser(optionalUser.get());
            availability.setCreatedAt(Instant.now());
            availabilities.add(availability);
        }

        availabilityRepository.saveAll(availabilities);
    }

    @Override
    public Pair<LocalTime, LocalTime> getSchedule(Schedule schedule) {
        LocalTime startTime;
        LocalTime endTime;
        switch (schedule) {
            case MORNING:
                startTime = LocalTime.of(8, 0);
                endTime = LocalTime.of(12, 0);
                break;
            case AFTERNOON:
                startTime = LocalTime.of(13, 0);
                endTime = LocalTime.of(17, 0);
                break;
            case FULLDAY:
                startTime = LocalTime.of(8, 0);
                endTime = LocalTime.of(17, 0);
                break;
            default: throw new UnknownScheduleException();
        }
        return Pair.of(startTime, endTime);
    }

    @Override
    public List<Pair<LocalTime, LocalTime>> generateTimes(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            //TODO create exception
            throw new IllegalArgumentException();
        }

        List<Pair<LocalTime, LocalTime>> times = new ArrayList<>();

        while (startTime.isBefore(endTime)) {
            LocalTime temporalEndTime = startTime.plusMinutes(60);
            Pair<LocalTime, LocalTime> time = Pair.of(startTime, temporalEndTime);
            times.add(time);
            startTime = temporalEndTime;
        }

        return times;
    }

    @Override
    public AvailabilityResponse getAvailabilities(LocalDate startDate, User user) {

        LocalDate endDate = startDate.plusDays(7);
        List<Availability> availabilities = availabilityRepository.findAllByUserAndDateBetweenOrderByDate(user, startDate, endDate);
        Map<LocalDate, List<Availability>> listMap = availabilities.stream().collect(Collectors.groupingBy(Availability::getDate));

        long[][] dates = new long[7][9];
        int i = 0;

        while (startDate.isBefore(endDate)) {
            List<Availability> availabilityDates = listMap.get(startDate);
            if (availabilityDates == null) {
                Arrays.fill(dates[i], 0);
            } else {
                List<Pair<LocalTime, LocalTime>> times = generateTimes(LocalTime.of(8, 0), LocalTime.of(17, 0));
                int j = 0;

                for (Pair<LocalTime, LocalTime> time : times) {
                    Optional<Availability> optionalAvailability = availabilityDates.stream()
                            .filter(availability -> availability.getStartTime().equals(time.getFirst()) && availability.getEndTime().equals(time.getSecond()))
                            .findFirst();

                    if (optionalAvailability.isPresent()) {
                        dates[i][j] = optionalAvailability.get().getId();
                    } else {
                        dates[i][j] = 0;
                    }
                    j++;
                }
            }
            startDate = startDate.plusDays(1);
            i++;
        }

        return AvailabilityResponse.builder()
                .previousStartDate(startDate.minusWeeks(1))
                .nextStartDate(startDate.plusWeeks(1))
                .availabilities(dates)
                .build();
    }

    @Override
    public Optional<Availability> findById(long id) {
        Optional<Availability> optionalAvailability = availabilityRepository.findById(id);
        LocalTime startTime = optionalAvailability.get().getStartTime();
        if (LocalTime.now().isAfter(startTime)) {
            throw new AvailabilityException();
        }
        return optionalAvailability;
    }

}
