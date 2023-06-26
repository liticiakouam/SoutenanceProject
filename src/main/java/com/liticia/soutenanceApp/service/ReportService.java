package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.dto.ReportCreate;

public interface ReportService {
    void save(ReportCreate reportCreate, long id);
}
