package com.jp.business.controller;

import com.jp.business.dto.EnquiryRequest;
import com.jp.business.entity.Enquiry;
import com.jp.business.entity.EnquiryStatus;
import com.jp.business.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
@CrossOrigin(origins = "http://localhost:5173")
public class EnquiryController {

    private final EnquiryService enquiryService;

    public EnquiryController(
            EnquiryService enquiryService
    ) {
        this.enquiryService = enquiryService;
    }

    @PostMapping
    public ResponseEntity<Enquiry> createEnquiry(
            @Valid @RequestBody EnquiryRequest request
    ) {

        Enquiry enquiry =
                enquiryService.createEnquiry(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enquiry);
    }

    @GetMapping
    public ResponseEntity<List<Enquiry>> getAllEnquiries() {

        return ResponseEntity.ok(
                enquiryService.getAllEnquiries()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enquiry> getEnquiry(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                enquiryService.getEnquiryById(id)
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Enquiry>>
    getCustomerEnquiries(
            @PathVariable Long customerId
    ) {

        return ResponseEntity.ok(
                enquiryService
                        .getCustomerEnquiries(customerId)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Enquiry> updateStatus(
            @PathVariable Long id,
            @RequestParam EnquiryStatus status
    ) {

        return ResponseEntity.ok(
                enquiryService.updateStatus(
                        id,
                        status
                )
        );
    }
}