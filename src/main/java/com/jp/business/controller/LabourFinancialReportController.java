package com.jp.business.controller;

import com.jp.business.dto.LabourFinancialReportResponse;
import com.jp.business.service.LabourFinancialReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/labour-reports")
@CrossOrigin(origins = "http://localhost:5173")
public class LabourFinancialReportController {

    private final LabourFinancialReportService reportService;

    public LabourFinancialReportController(
            LabourFinancialReportService reportService
    ) {
        this.reportService = reportService;
    }

    @GetMapping("/{labourId}/financial")
    public ResponseEntity<LabourFinancialReportResponse>
    getFinancialReport(
            @PathVariable Long labourId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        LabourFinancialReportResponse response =
                reportService.getFinancialReport(
                        labourId,
                        startDate,
                        endDate
                );

        return ResponseEntity.ok(response);
    }
}