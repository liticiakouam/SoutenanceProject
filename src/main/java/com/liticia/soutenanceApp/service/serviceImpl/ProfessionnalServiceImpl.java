package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.ProfessionnalRepository;
import com.liticia.soutenanceApp.service.ProfessionnalService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfessionnalServiceImpl implements ProfessionnalService {
    private ProfessionnalRepository professionnalRepository;


    @Override
    public Page<User> findAll(Pageable pageable) {
        return professionnalRepository.findAllByRolesIdOrderByCreatedAtDesc(pageable, 3);
    }

    @Override
    public Optional<User> findById(long id) {
        return professionnalRepository.findById(id);
    }

    @Override
    public List<User> searchUser(String city, String speciality, String keyword) {
        return professionnalRepository.searchUsers(city, speciality, keyword);
    }

    @Override
    public List<User> findUsers() {
        return null;
    }

}
