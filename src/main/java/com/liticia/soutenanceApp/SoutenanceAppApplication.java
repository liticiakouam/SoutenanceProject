package com.liticia.soutenanceApp;

import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class SoutenanceAppApplication implements CommandLineRunner {
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(SoutenanceAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Role admin = Role.builder().id(1).name("ADMIN").build();
        Role user = Role.builder().id(2).name("CLIENT").build();
        Role professional = Role.builder().id(3).name("PROFESSIONAL").build();
        roleRepository.saveAll(List.of(admin, user, professional));

    }
}