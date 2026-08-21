package com.jp.business.controller;

import com.jp.business.dto.SiteVisitRequest;
import com.jp.business.dto.SiteVisitResponse;
import com.jp.business.entity.SiteVisit;
import com.jp.business.entity.SiteVisitStatus;
import com.jp.business.service.SiteVisitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jp.business.dto.SiteVisitResponse;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/site-visits")
@CrossOrigin(origins = "http://localhost:5173")
public class SiteVisitController {

    private final SiteVisitService siteVisitService;

    public SiteVisitController(
            SiteVisitService siteVisitService
    ) {
        this.siteVisitService = siteVisitService;
    }

    @PostMapping
    public ResponseEntity<SiteVisitResponse> createSiteVisit(
            @Valid @RequestBody SiteVisitRequest request
    ) {
        System.out.println(">>> SITE VISIT CONTROLLER REACHED");

       SiteVisit siteVisit =
        siteVisitService.createSiteVisit(request);

SiteVisitResponse response =
        siteVisitService.toResponse(siteVisit);

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
    }

    @GetMapping
public ResponseEntity<List<SiteVisitResponse>> getAllSiteVisits() {

    List<SiteVisitResponse> response =
            siteVisitService.getAllSiteVisits()
                    .stream()
                    .map(siteVisitService::toResponse)
                    .collect(Collectors.toList());

    return ResponseEntity.ok(response);
}

    @GetMapping("/{id}")
public ResponseEntity<SiteVisitResponse> getSiteVisit(
        @PathVariable Long id
) {

    SiteVisit siteVisit =
            siteVisitService.getSiteVisitById(id);

    return ResponseEntity.ok(
            siteVisitService.toResponse(siteVisit)
    );
}
    @GetMapping("/enquiry/{enquiryId}")
public ResponseEntity<List<SiteVisitResponse>>
getEnquirySiteVisits(
        @PathVariable Long enquiryId
) {

    List<SiteVisitResponse> response =
            siteVisitService
                    .getEnquirySiteVisits(enquiryId)
                    .stream()
                    .map(siteVisitService::toResponse)
                    .collect(Collectors.toList());

    return ResponseEntity.ok(response);
}

    @GetMapping("/date/{date}")
public ResponseEntity<List<SiteVisitResponse>>
getVisitsByDate(
        @PathVariable LocalDate date
) {

    List<SiteVisitResponse> response =
            siteVisitService
                    .getVisitsByDate(date)
                    .stream()
                    .map(siteVisitService::toResponse)
                    .collect(Collectors.toList());

    return ResponseEntity.ok(response);
}

  @PatchMapping("/{id}/status")
public ResponseEntity<SiteVisitResponse> updateStatus(
        @PathVariable Long id,
        @RequestParam SiteVisitStatus status
) {

    SiteVisit siteVisit =
            siteVisitService.updateStatus(
                    id,
                    status
            );

    return ResponseEntity.ok(
            siteVisitService.toResponse(siteVisit)
    );
}
}