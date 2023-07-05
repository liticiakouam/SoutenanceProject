package com.liticia.soutenanceApp.repository;

import com.liticia.soutenanceApp.model.DemandeCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends JpaRepository<DemandeCompte, Long> {
}
