package com.liticia.soutenanceApp.service.serviceImpl;

import com.liticia.soutenanceApp.model.DemandeCompte;
import com.liticia.soutenanceApp.repository.DemandRepository;
import com.liticia.soutenanceApp.service.DemandService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandServiceImpl implements DemandService {

    private final DemandRepository demandRepository;

    public DemandServiceImpl(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }

    @Override
    public List<DemandeCompte> findAll() {
        return demandRepository.findAll();
    }
}
