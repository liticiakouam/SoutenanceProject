package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.Report;
import com.liticia.soutenanceApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAppointmentByAvailabilityId(Long availabilityId);

    Optional<Appointment> findByUserCustomerAndCreatedAt(User user, Instant dateTime);

    List<Appointment> findAllByUserCustomerAndReport(User user, Report report);
    Page<Appointment> findAllByUserCustomerAndReportOrderByCreatedAtDesc(User user, Report report, Pageable pageable);
}
