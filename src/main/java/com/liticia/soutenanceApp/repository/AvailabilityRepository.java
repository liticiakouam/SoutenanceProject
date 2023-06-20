package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findAllByUserAndDateBetweenOrderByDate(User user, LocalDate startDate, LocalDate endDate);
    List<Availability> findByUser(User user);
}
