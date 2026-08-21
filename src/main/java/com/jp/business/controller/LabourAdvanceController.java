package com.jp.business.controller;

import com.jp.business.dto.LabourAdvanceSummaryResponse;
import com.jp.business.service.LabourAdvanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/labour-advances")
@CrossOrigin(origins = "http://localhost:5173")
public class LabourAdvanceController {

    private final LabourAdvanceService advanceService;

    public LabourAdvanceController(
            LabourAdvanceService advanceService
    ) {
        this.advanceService = advanceService;
    }

    @GetMapping("/labour/{labourId}/summary")
    public ResponseEntity<LabourAdvanceSummaryResponse>
    getAdvanceSummary(
            @PathVariable Long labourId
    ) {

        LabourAdvanceSummaryResponse response =
                advanceService.getAdvanceSummary(labourId);

        return ResponseEntity.ok(response);
    }
}