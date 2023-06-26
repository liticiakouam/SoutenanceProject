package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.Report;
import com.liticia.soutenanceApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAppointmentByAvailabilityId(Long availabilityId);

    Optional<Appointment> findByUserCustomerAndCreatedAt(User user, Instant dateTime);

    @Query("select a from Appointment a where a.availability.date < :now AND a.userCustomer.id=:userId AND a.report = null ORDER BY a.createdAt DESC")
    List<Appointment> findIncompletedAppointementByDate(LocalDate now, long userId);

    Page<Appointment> findAllByUserCustomerAndReportOrderByCreatedAtDesc(User user, Report report, Pageable pageable);

    @Query("select a from Appointment a where a.availability.date < :now AND a.userCustomer.id=:userId AND a.report != null AND a.deleted = false ORDER BY a.createdAt DESC")
    List<Appointment> findOldAppointmentByDate(LocalDate now, long userId);

    @Query("select a from Appointment a where a.availability.date > :now AND a.userCustomer.id=:userId AND a.report = null AND a.deleted = false ORDER BY a.createdAt DESC")
    List<Appointment> findAppointmentToComeByDate(LocalDate now, long userId);

    @Query("select a from Appointment a where a.availability.date < :now AND a.report != null AND a.deleted = false ORDER BY a.createdAt DESC")
    Page<Appointment> findAppointmentByDateOrderBy(LocalDate now, Pageable pageable);
}
