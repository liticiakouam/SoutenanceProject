package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findAllByDateBetweenOrderByDate(LocalDate startDate, LocalDate endDate);
}
