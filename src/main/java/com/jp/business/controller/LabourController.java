package com.jp.business.controller;

import com.jp.business.dto.LabourRequest;
import com.jp.business.dto.LabourResponse;
import com.jp.business.entity.Labour;
import com.jp.business.service.LabourService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/labours")
@CrossOrigin(origins = "http://localhost:5173")
public class LabourController {

    private final LabourService labourService;

    public LabourController(
            LabourService labourService
    ) {
        this.labourService = labourService;
    }

    @PostMapping
    public ResponseEntity<LabourResponse> createLabour(
            @Valid @RequestBody LabourRequest request
    ) {

        Labour labour =
                labourService.createLabour(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        labourService.toResponse(labour)
                );
    }

    @GetMapping
    public ResponseEntity<List<LabourResponse>>
    getAllLabours() {

        List<LabourResponse> response =
                labourService
                        .getAllLabours()
                        .stream()
                        .map(labourService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<LabourResponse>>
    getActiveLabours() {

        List<LabourResponse> response =
                labourService
                        .getActiveLabours()
                        .stream()
                        .map(labourService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<LabourResponse>>
    getInactiveLabours() {

        List<LabourResponse> response =
                labourService
                        .getInactiveLabours()
                        .stream()
                        .map(labourService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabourResponse>
    getLabour(
            @PathVariable Long id
    ) {

        Labour labour =
                labourService.getLabourById(id);

        return ResponseEntity.ok(
                labourService.toResponse(labour)
        );
    }

    @GetMapping("/number/{labourNo}")
    public ResponseEntity<LabourResponse>
    getLabourByNumber(
            @PathVariable String labourNo
    ) {

        Labour labour =
                labourService.getLabourByNo(labourNo);

        return ResponseEntity.ok(
                labourService.toResponse(labour)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabourResponse>
    updateLabour(
            @PathVariable Long id,
            @Valid @RequestBody LabourRequest request
    ) {

        Labour labour =
                labourService.updateLabour(
                        id,
                        request
                );

        return ResponseEntity.ok(
                labourService.toResponse(labour)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void>
    deactivateLabour(
            @PathVariable Long id
    ) {

        labourService.deactivateLabour(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void>
    activateLabour(
            @PathVariable Long id
    ) {

        labourService.activateLabour(id);

        return ResponseEntity.noContent().build();
    }
}