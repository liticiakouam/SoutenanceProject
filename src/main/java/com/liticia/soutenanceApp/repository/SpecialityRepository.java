package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.City;
import com.liticia.soutenanceApp.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
}
