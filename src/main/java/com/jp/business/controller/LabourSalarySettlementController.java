package com.jp.business.controller;

import com.jp.business.dto.LabourSalarySettlementRequest;
import com.jp.business.dto.LabourSalarySettlementResponse;
import com.jp.business.entity.LabourSalarySettlement;
import com.jp.business.service.LabourSalarySettlementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/labour-settlements")
@CrossOrigin(origins = "http://localhost:5173")
public class LabourSalarySettlementController {

    private final LabourSalarySettlementService settlementService;

    public LabourSalarySettlementController(
            LabourSalarySettlementService settlementService
    ) {
        this.settlementService = settlementService;
    }

    @PostMapping
    public ResponseEntity<LabourSalarySettlementResponse>
    createSettlement(
            @Valid
            @RequestBody
            LabourSalarySettlementRequest request
    ) {

        LabourSalarySettlement settlement =
                settlementService.createSettlement(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        settlementService.toResponse(settlement)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabourSalarySettlementResponse>
    getSettlement(
            @PathVariable Long id
    ) {

        LabourSalarySettlement settlement =
                settlementService.getSettlementById(id);

        return ResponseEntity.ok(
                settlementService.toResponse(settlement)
        );
    }

    @GetMapping("/labour/{labourId}")
    public ResponseEntity<List<LabourSalarySettlementResponse>>
    getLabourSettlements(
            @PathVariable Long labourId
    ) {

        List<LabourSalarySettlementResponse> response =
                settlementService
                        .getLabourSettlements(labourId)
                        .stream()
                        .map(settlementService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/recalculate")
    public ResponseEntity<LabourSalarySettlementResponse>
    recalculateSettlement(
            @PathVariable Long id
    ) {

        LabourSalarySettlement settlement =
                settlementService.recalculateSettlement(id);

        return ResponseEntity.ok(
                settlementService.toResponse(settlement)
        );
    }
}