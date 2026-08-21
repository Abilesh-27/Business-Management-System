package com.jp.business.controller;

import com.jp.business.dto.LabourSalarySummaryResponse;
import com.jp.business.service.LabourSalaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/labour-salary")
@CrossOrigin(origins = "http://localhost:5173")
public class LabourSalaryController {

    private final LabourSalaryService salaryService;

    public LabourSalaryController(
            LabourSalaryService salaryService
    ) {
        this.salaryService = salaryService;
    }

    @GetMapping("/{labourId}/summary")
    public ResponseEntity<LabourSalarySummaryResponse>
    getSalarySummary(
            @PathVariable Long labourId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        LabourSalarySummaryResponse response =
                salaryService.getSalarySummary(
                        labourId,
                        startDate,
                        endDate
                );

        return ResponseEntity.ok(response);
    }
}