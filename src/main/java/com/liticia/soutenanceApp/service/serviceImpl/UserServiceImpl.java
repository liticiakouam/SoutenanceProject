package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.dto.DemandeCreate;
import com.liticia.soutenanceApp.dto.ProfessionalCreate;
import com.liticia.soutenanceApp.dto.CustomerDto;
import com.liticia.soutenanceApp.dto.UpdateUserDto;
import com.liticia.soutenanceApp.exception.EmailAlreadyExistException;
import com.liticia.soutenanceApp.model.*;
import com.liticia.soutenanceApp.repository.*;
import com.liticia.soutenanceApp.security.SecurityUtils;
import com.liticia.soutenanceApp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private DemandRepository demandRepository;
    private PasswordEncoder passwordEncoder;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private SecurityUtils securityUtils;

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/document";

    @Override
    public Optional<User> findById() {
        return userRepository.findById(securityUtils.getCurrentUserId());
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
        Role pro = roleRepository.findById(3L).get();
        String password = UUID.randomUUID().toString();
        User userByEmail = userRepository.findUserByEmail(professionalCreate.getEmail());
        if (userByEmail != null) {
            throw new EmailAlreadyExistException();
        }

        List<Role> roles = new ArrayList<>();
        roles.add(pro);
        User user = new User();
        user.setEmail(professionalCreate.getEmail());
        user.setFirstName(professionalCreate.getFirstName());
        user.setLastName(professionalCreate.getLastName());
        user.setDomain(professionalCreate.getDomain());
        user.setCity(professionalCreate.getCity());
        user.setPassWord(passwordEncoder.encode(password));
        user.setSpeciality(professionalCreate.getSpeciality());
        user.setRoles(roles);
        user.setCreatedAt(Instant.now());
        user.setPhone(professionalCreate.getPhone());
        user.setAddress(professionalCreate.getAddress());

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
        demandeCompte.setDomain(demandeCreate.getDomain());
        demandeCompte.setAddress(demandeCreate.getAddress());
        demandeCompte.setPhone(demandeCompte.getPhone());

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

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void saveCustomer(CustomerDto customerDto) {
        User existingUser = userRepository.findUserByEmail(customerDto.getEmail());
        if (existingUser != null) {
            throw new EmailAlreadyExistException();
        }
        User user = new User();
        Role role = roleRepository.findById(2L).get();

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setLastName(customerDto.getLastname());
        user.setFirstName(customerDto.getFirstname());
        user.setEmail(customerDto.getEmail());
        user.setPassWord(passwordEncoder.encode(customerDto.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public void updateUserInformations(UpdateUserDto updateUserDto) {
        User user = userRepository.findById(securityUtils.getCurrentUserId()).get();

        user.setDetails(updateUserDto.getDetails());
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setPhone(updateUserDto.getPhone());
        user.setAddress(updateUserDto.getAddress());

        userRepository.save(user);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            return false;
        }
        return !passwordResetToken.isExpired();
    }

    @Override
    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            return null;
        }
        return passwordResetToken.getUser();
    }

    @Override
    public void changeUserPassword(User user, String password) {
        user.setPassWord(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void deletePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken != null) {
            passwordResetTokenRepository.delete(passwordResetToken);
        }
    }

//    @Override
//    public User updateUserInformations(UpdateUserDto updateUserDto) {
//        User user = userRepository.findById(SecurityUtils.getCurrentUserId()).get();
//
//        user.setId(SecurityUtils.getCurrentUserId());
//        user.setAddress(updateUserDto.getAddress());
//        user.setDetails(updateUserDto.getDetails());
//        user.setFirstName(updateUserDto.getFirstName());
//        user.setLastName(updateUserDto.getLastName());
//        user.setPhone(updateUserDto.getPhone());
//
//        return userRepository.save(user);
//    }
}
