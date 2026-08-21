package com.jp.business.controller;

import com.jp.business.dto.LabourDashboardResponse;
import com.jp.business.service.LabourDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/labour-dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class LabourDashboardController {

    private final LabourDashboardService dashboardService;

    public LabourDashboardController(
            LabourDashboardService dashboardService
    ) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/today")
    public ResponseEntity<LabourDashboardResponse>
    getTodayDashboard() {

        LabourDashboardResponse response =
                dashboardService.getTodayDashboard();

        return ResponseEntity.ok(response);
    }
}