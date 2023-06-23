package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAppointmentByAvailabilityId(Long availabilityId);

    Optional<Appointment> findByUserCustomerAndCreatedAt(User user, Instant dateTime);
}
