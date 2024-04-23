package com.liticia.soutenanceApp;

import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.repository.RoleRepository;
import com.liticia.soutenanceApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class SoutenanceAppApplication implements CommandLineRunner {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SoutenanceAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Role admin = Role.builder().id(1).name("ADMIN").build();
        Role user = Role.builder().id(2).name("CLIENT").build();
        Role professional = Role.builder().id(3).name("PROFESSIONAL").build();
        roleRepository.saveAll(List.of(admin, user, professional));

        User adminUser = new User(1L, "Admin", "Admin", "admin@gmail.com", null, passwordEncoder.encode("admin@123"), null, null, null, null, 663673763, Instant.now(), null, null, List.of(admin));
        userRepository.save(adminUser);
    }
}