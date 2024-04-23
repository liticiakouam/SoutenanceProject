package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionnalRepository extends JpaRepository<User, Long> {
    Page<User> findAllByRolesIdOrderByCreatedAtDesc(Pageable pageable, long id);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE (u.city = :city OR u.speciality = :speciality OR u.lastName = :keyword OR u.firstName = :keyword) AND r.id = 3")
    List<User> searchUsers(@Param("city") String city, @Param("speciality") String speciality, @Param("keyword") String keyword);

}
