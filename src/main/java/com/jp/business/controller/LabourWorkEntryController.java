package com.jp.business.controller;

import com.jp.business.dto.LabourWorkEntryRequest;
import com.jp.business.dto.LabourWorkEntryResponse;
import com.jp.business.entity.LabourWorkEntry;
import com.jp.business.service.LabourWorkEntryService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/labour-work")
@CrossOrigin(origins = "http://localhost:5173")
public class LabourWorkEntryController {

    private final LabourWorkEntryService workEntryService;

    public LabourWorkEntryController(
            LabourWorkEntryService workEntryService
    ) {
        this.workEntryService = workEntryService;
    }

    @PostMapping
    public ResponseEntity<LabourWorkEntryResponse> createWorkEntry(
            @Valid @RequestBody LabourWorkEntryRequest request
    ) {

        LabourWorkEntry entry =
                workEntryService.createWorkEntry(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workEntryService.toResponse(entry));
    }

    @GetMapping
    public ResponseEntity<List<LabourWorkEntryResponse>>
    getAllWorkEntries() {

        List<LabourWorkEntryResponse> response =
                workEntryService
                        .getAllWorkEntries()
                        .stream()
                        .map(workEntryService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabourWorkEntryResponse>
    getWorkEntry(
            @PathVariable Long id
    ) {

        LabourWorkEntry entry =
                workEntryService.getWorkEntryById(id);

        return ResponseEntity.ok(
                workEntryService.toResponse(entry)
        );
    }

    @GetMapping("/labour/{labourId}")
    public ResponseEntity<List<LabourWorkEntryResponse>>
    getLabourWorkHistory(
            @PathVariable Long labourId
    ) {

        List<LabourWorkEntryResponse> response =
                workEntryService
                        .getLabourWorkHistory(labourId)
                        .stream()
                        .map(workEntryService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<LabourWorkEntryResponse>>
    getWorkEntriesByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {

        List<LabourWorkEntryResponse> response =
                workEntryService
                        .getWorkEntriesByDate(date)
                        .stream()
                        .map(workEntryService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/labour/{labourId}/between")
    public ResponseEntity<List<LabourWorkEntryResponse>>
    getLabourWorkBetweenDates(
            @PathVariable Long labourId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        List<LabourWorkEntryResponse> response =
                workEntryService
                        .getLabourWorkBetweenDates(
                                labourId,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(workEntryService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/site/{siteId}/between")
    public ResponseEntity<List<LabourWorkEntryResponse>>
    getSiteWorkBetweenDates(
            @PathVariable Long siteId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        List<LabourWorkEntryResponse> response =
                workEntryService
                        .getSiteWorkBetweenDates(
                                siteId,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(workEntryService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}