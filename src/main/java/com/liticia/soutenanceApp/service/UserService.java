package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.SearchUserDto;
import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import com.liticia.soutenanceApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<User> findAll(Pageable pageable);
    Optional<User> findById(long id);

    List<User> searchUser(String city, String speciality, String keyword);
}
