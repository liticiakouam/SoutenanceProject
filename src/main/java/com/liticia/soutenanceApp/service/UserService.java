package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.DemandeCreate;
import com.liticia.soutenanceApp.dto.ProfessionalCreate;
import com.liticia.soutenanceApp.dto.CustomerDto;
import com.liticia.soutenanceApp.dto.UpdateUserDto;
import com.liticia.soutenanceApp.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById();
    List<User> findClients();
    List<User> findProfessionals();
    void saveProfessional(ProfessionalCreate professionalCreate);
    void saveProfessionalDemand(DemandeCreate demandeCreate, MultipartFile file, String document) throws IOException;
    User findUserByEmail(String email);
    void saveCustomer(CustomerDto customerDto);

    void updateUserInformations(UpdateUserDto updateUserDto);

    void createPasswordResetTokenForUser(User user, String token);

    boolean validatePasswordResetToken(String token);

    User getUserByPasswordResetToken(String token);

    void changeUserPassword(User user, String password);

    void deletePasswordResetToken(String token);
}
