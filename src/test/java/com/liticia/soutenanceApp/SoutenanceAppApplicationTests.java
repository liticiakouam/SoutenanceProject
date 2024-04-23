package com.liticia.soutenanceApp;

import com.liticia.soutenanceApp.config.TestConfig;
import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@Import(TestConfig.class)
class SoutenanceAppApplicationTests implements CommandLineRunner {
	private PasswordEncoder bCryptPasswordEncoder;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private AvailabilityRepository availabilityRepository;
	private AppointmentRepository appointmentRepository;

	@Test
	void contextLoads() {
	}

	@Override
	public void run(String... args) throws Exception {
		Role admin = Role.builder().id(1).name("ADMIN").build();
		Role user = Role.builder().id(2).name("CLIENT").build();
		Role professional = Role.builder().id(3).name("PROFESSIONAL").build();
		roleRepository.saveAll(List.of(admin, user, professional));

		User user1 = User.builder().id(4L).email("admin@gmail.com").passWord(bCryptPasswordEncoder.encode("1234")).roles(List.of(admin)).firstName("liticia").build();
		User user2 = User.builder().id(2L).email("liticiakouam@gmail.com").passWord(bCryptPasswordEncoder.encode("1234")).roles(List.of(user)).firstName("liticia").build();
		User user3 = User.builder().id(3L).email("anzeliticia@gmail.com").passWord(bCryptPasswordEncoder.encode("1234")).roles(List.of(professional)).firstName("anze").city("douala").speciality("medecine").build();
		User user4 = User.builder().id(1L).email("user@gmail.com").passWord(bCryptPasswordEncoder.encode("1234")).roles(List.of(professional)).firstName("anze").city("douala").speciality("medecine").build();
		userRepository.saveAll(List.of(user2, user1, user3, user4));

		Availability availability1 = Availability.builder().id(73L).user(user3).date(LocalDate.of(2024, 03, 03)).startTime(LocalTime.of(8, 10)).endTime(LocalTime.of(12, 0)).build();
		Availability availability2 = Availability.builder().user(user2).date(LocalDate.now()).startTime(LocalTime.now()).endTime(LocalTime.of(12, 0)).build();
		availabilityRepository.saveAll(List.of(availability1, availability2));

		Report report = Report.builder().note("merci").build();
		Appointment appointment1 = Appointment.builder().availability(availability1).userPro(user3).userCustomer(user2).reportPro(report).report(report).description("consultation").build();
		Appointment appointment2 = Appointment.builder().availability(availability2).userPro(user3).userCustomer(user2).description("consultation").build();
		appointmentRepository.saveAll(List.of(appointment2, appointment1));
	}
}
