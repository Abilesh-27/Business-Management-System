package com.jp.business.controller;

import com.jp.business.dto.QuotationRequest;
import com.jp.business.dto.QuotationResponse;
import com.jp.business.entity.Quotation;
import com.jp.business.service.QuotationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quotations")
@CrossOrigin(origins = "http://localhost:5173")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(
            QuotationService quotationService
    ) {
        this.quotationService = quotationService;
    }

    @PostMapping
    public ResponseEntity<QuotationResponse> createQuotation(
            @Valid @RequestBody QuotationRequest request
    ) {

        Quotation quotation =
                quotationService.createQuotation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        quotationService.toResponse(quotation)
                );
    }

    @GetMapping
    public ResponseEntity<List<QuotationResponse>>
    getAllQuotations() {

        List<QuotationResponse> response =
                quotationService
                        .getAllQuotations()
                        .stream()
                        .map(quotationService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotationResponse>
    getQuotation(
            @PathVariable Long id
    ) {

        Quotation quotation =
                quotationService
                        .getQuotationById(id);

        return ResponseEntity.ok(
                quotationService.toResponse(quotation)
        );
    }

    @GetMapping("/enquiry/{enquiryId}")
    public ResponseEntity<List<QuotationResponse>>
    getQuotationsByEnquiry(
            @PathVariable Long enquiryId
    ) {

        List<QuotationResponse> response =
                quotationService
                        .getQuotationsByEnquiry(enquiryId)
                        .stream()
                        .map(quotationService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<QuotationResponse>>
    getQuotationsByStatus(
            @PathVariable String status
    ) {

        List<QuotationResponse> response =
                quotationService
                        .getQuotationsByStatus(status.toUpperCase())
                        .stream()
                        .map(quotationService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<QuotationResponse>
    updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {

        Quotation quotation =
                quotationService.updateStatus(
                        id,
                        status
                );

        return ResponseEntity.ok(
                quotationService.toResponse(quotation)
        );
    }
}