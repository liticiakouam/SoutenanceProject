package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionnalRepository extends JpaRepository<User, Long> {
    Page<User> findAllByRolesIdOrderByCreatedAtDesc(Pageable pageable, long id);

    @Query("select u from User u where u.city.name=:city or u.speciality.name=:speciality or u.lastName=:keyword or u.firstName=:keyword")
    List<User> searchUsers(String city, String speciality, String keyword);

}
