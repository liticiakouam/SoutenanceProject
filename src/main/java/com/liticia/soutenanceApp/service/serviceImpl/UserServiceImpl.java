package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.dto.DemandeCreate;
import com.liticia.soutenanceApp.dto.ProfessionalCreate;
import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.repository.*;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final SpecialityRepository specialityRepository;
    private final RoleRepository roleRepository;
    private final DemandRepository demandRepository;
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/document";

    public UserServiceImpl(UserRepository userRepository, CityRepository cityRepository, SpecialityRepository specialityRepository, RoleRepository roleRepository, DemandRepository demandRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.specialityRepository = specialityRepository;
        this.roleRepository = roleRepository;
        this.demandRepository = demandRepository;
    }

    @Override
    public Optional<User> findById() {
        return userRepository.findById(SecurityUtils.getCurrentUserId());
    }

    @Override
    public List<User> findClients() {
        return userRepository.findByRolesIdOrderByCreatedAtDesc(2);
    }

    @Override
    public List<User> findProfessionals() {
        return userRepository.findByRolesIdOrderByCreatedAtDesc(3);
    }

    @Override
    public void saveProfessional(ProfessionalCreate professionalCreate) {
        City city = cityRepository.findById(professionalCreate.getCityId()).get();
        Speciality speciality = specialityRepository.findById(professionalCreate.getSpecialityId()).get();
        Role role = roleRepository.findById(3L).get();

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        User user = new User();
        user.setEmail(professionalCreate.getEmail());
        user.setFirstName(professionalCreate.getFirstName());
        user.setLastName(professionalCreate.getLastName());
        user.setDomain(professionalCreate.getDomain());
        user.setCity(city);
        user.setSpeciality(speciality);
        user.setRoles(roles);
        user.setCreatedAt(Instant.now());

        userRepository.save(user);
    }

    @Override
    public void saveProfessionalDemand(DemandeCreate demandeCreate, MultipartFile file, String document) throws IOException {
        DemandeCompte demandeCompte = new DemandeCompte();
        demandeCompte.setEmail(demandeCreate.getEmail());
        demandeCompte.setFirstName(demandeCreate.getFirstName());
        demandeCompte.setLastName(demandeCreate.getLastName());
        demandeCompte.setCity(demandeCreate.getCity());
        demandeCompte.setSpeciality(demandeCreate.getSpeciality());
        demandeCompte.setCreatedAt(Instant.now());

        String documentUUID;
        if (!file.isEmpty()) {
            documentUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, documentUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            documentUUID = document;
        }
        demandeCompte.setDocument(documentUUID);

        demandRepository.save(demandeCompte);
    }
}
