package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.model.DemandeCompte;

import java.util.List;

public interface DemandService {
   List<DemandeCompte> findAll();

    void delete(long id);
}
