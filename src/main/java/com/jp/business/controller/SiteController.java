package com.jp.business.controller;

import com.jp.business.dto.SiteRequest;
import com.jp.business.dto.SiteResponse;
import com.jp.business.entity.Site;
import com.jp.business.service.SiteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sites")
@CrossOrigin(origins = "http://localhost:5173")
public class SiteController {

    private final SiteService siteService;

    public SiteController(
            SiteService siteService
    ) {
        this.siteService = siteService;
    }

    @PostMapping
    public ResponseEntity<SiteResponse> createSite(
            @Valid @RequestBody SiteRequest request
    ) {

        Site site =
                siteService.createSite(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        siteService.toResponse(site)
                );
    }

    @GetMapping
    public ResponseEntity<List<SiteResponse>>
    getAllSites() {

        List<SiteResponse> response =
                siteService
                        .getAllSites()
                        .stream()
                        .map(siteService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SiteResponse>>
    getActiveSites() {

        List<SiteResponse> response =
                siteService
                        .getActiveSites()
                        .stream()
                        .map(siteService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<SiteResponse>>
    getInactiveSites() {

        List<SiteResponse> response =
                siteService
                        .getInactiveSites()
                        .stream()
                        .map(siteService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteResponse>
    getSite(
            @PathVariable Long id
    ) {

        Site site =
                siteService.getSiteById(id);

        return ResponseEntity.ok(
                siteService.toResponse(site)
        );
    }

    @GetMapping("/name/{siteName}")
    public ResponseEntity<SiteResponse>
    getSiteByName(
            @PathVariable String siteName
    ) {

        Site site =
                siteService.getSiteByName(siteName);

        return ResponseEntity.ok(
                siteService.toResponse(site)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SiteResponse>
    updateSite(
            @PathVariable Long id,
            @Valid @RequestBody SiteRequest request
    ) {

        Site site =
                siteService.updateSite(
                        id,
                        request
                );

        return ResponseEntity.ok(
                siteService.toResponse(site)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void>
    deactivateSite(
            @PathVariable Long id
    ) {

        siteService.deactivateSite(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void>
    activateSite(
            @PathVariable Long id
    ) {

        siteService.activateSite(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}