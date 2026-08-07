package com.msme.erp.controller;

import com.msme.erp.dto.DashboardKpiDto;
import com.msme.erp.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/kpis")
    public ResponseEntity<DashboardKpiDto> getDashboardKpis() {
        return ResponseEntity.ok(dashboardService.getDashboardKpis());
    }
}
