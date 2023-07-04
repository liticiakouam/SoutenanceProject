package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRolesIdOrderByCreatedAtDesc(long id);
}
