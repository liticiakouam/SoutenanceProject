package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.dto.AppointmentCreate;
import com.liticia.soutenanceApp.exception.AvailabilityException;
import com.liticia.soutenanceApp.exception.UserNotFoundException;
import com.liticia.soutenanceApp.model.Appointment;
import com.liticia.soutenanceApp.model.Availability;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.AppointmentRepository;
import com.liticia.soutenanceApp.repository.AvailabilityRepository;
import com.liticia.soutenanceApp.repository.UserRepository;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;

    private final AvailabilityRepository availabilityRepository;

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/document";

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, UserRepository userRepository, AvailabilityRepository availabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public void save(AppointmentCreate appointmentCreate, MultipartFile file, String document, long id) throws IOException {
        Optional<Availability> optionalAvailability = availabilityRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        Optional<User> optionalUserPro = userRepository.findById(optionalAvailability.get().getUser().getId());
        List<Appointment> appointments = appointmentRepository.findAppointmentByAvailabilityId(id);

        if (appointments.size() > 0) {
            throw new AvailabilityException();
        }
//        if (optionalUser.isEmpty() !!optionalUserPro.isEmpty() ) {
//            throw new UserNotFoundException();
//        }

        Appointment appointment = new Appointment();
        appointment.setAvailability(optionalAvailability.get());
        appointment.setUserCustomer(optionalUser.get());
        appointment.setUserPro(optionalUserPro.get());
        appointment.setPattern(appointmentCreate.getPattern());
        appointment.setDescription(appointmentCreate.getDescription());
        appointment.setCreatedAt(Instant.now());

        String documentUUID;
        if (!file.isEmpty()) {
            documentUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, documentUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            documentUUID = document;
        }
        appointment.setDocument(documentUUID);

        appointmentRepository.save(appointment);
    }

    @Override
    public Optional<Appointment> findByUserCustomerAndCreatedAt() {
        Instant date = Instant.now();
        Optional<User> userOptional = userRepository.findById(SecurityUtils.getCurrentUserId());
        return appointmentRepository.findByUserCustomerAndCreatedAt(userOptional.get(), date);
    }

    @Override
    public Page<Appointment> findPageByReportAndUser(Pageable pageable) {
        Optional<User> userOptional = userRepository.findById(SecurityUtils.getCurrentUserId());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }

        return appointmentRepository.findAllByUserCustomerAndReportOrderByCreatedAtDesc(userOptional.get(), null, pageable);
    }

    @Override
    public List<Appointment> findAllByReportAndUser() {
        Optional<User> userOptional = userRepository.findById(SecurityUtils.getCurrentUserId());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }

        return appointmentRepository.findAllByUserCustomerAndReport(userOptional.get(), null);
    }

    @Override
    public Optional<Appointment> findById(long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> findAppointmentByOldDate() {
        LocalDate now = LocalDate.now();
        return appointmentRepository.findAppointmentByDate(now, SecurityUtils.getCurrentUserId());
    }

}
