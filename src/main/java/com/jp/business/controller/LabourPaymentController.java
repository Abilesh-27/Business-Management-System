package com.jp.business.controller;

import com.jp.business.dto.LabourPaymentRequest;
import com.jp.business.dto.LabourPaymentResponse;
import com.jp.business.entity.LabourPayment;
import com.jp.business.entity.LabourPaymentType;
import com.jp.business.service.LabourPaymentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/labour-payments")
@CrossOrigin(origins = "http://localhost:5173")
public class LabourPaymentController {

    private final LabourPaymentService paymentService;

    public LabourPaymentController(
            LabourPaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<LabourPaymentResponse> createPayment(
            @Valid @RequestBody LabourPaymentRequest request
    ) {

        LabourPayment payment =
                paymentService.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        paymentService.toResponse(payment)
                );
    }

    @GetMapping
    public ResponseEntity<List<LabourPaymentResponse>>
    getAllPayments() {

        List<LabourPaymentResponse> response =
                paymentService
                        .getAllPayments()
                        .stream()
                        .map(paymentService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabourPaymentResponse>
    getPayment(
            @PathVariable Long id
    ) {

        LabourPayment payment =
                paymentService.getPaymentById(id);

        return ResponseEntity.ok(
                paymentService.toResponse(payment)
        );
    }

    @GetMapping("/labour/{labourId}")
    public ResponseEntity<List<LabourPaymentResponse>>
    getLabourPaymentHistory(
            @PathVariable Long labourId
    ) {

        List<LabourPaymentResponse> response =
                paymentService
                        .getLabourPaymentHistory(labourId)
                        .stream()
                        .map(paymentService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/labour/{labourId}/between")
    public ResponseEntity<List<LabourPaymentResponse>>
    getPaymentsBetweenDates(
            @PathVariable Long labourId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        List<LabourPaymentResponse> response =
                paymentService
                        .getPaymentsBetweenDates(
                                labourId,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(paymentService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/labour/{labourId}/type/{paymentType}")
    public ResponseEntity<List<LabourPaymentResponse>>
    getPaymentsByType(
            @PathVariable Long labourId,
            @PathVariable LabourPaymentType paymentType
    ) {

        List<LabourPaymentResponse> response =
                paymentService
                        .getPaymentsByType(
                                labourId,
                                paymentType
                        )
                        .stream()
                        .map(paymentService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/labour/{labourId}/type/{paymentType}/between")
    public ResponseEntity<List<LabourPaymentResponse>>
    getPaymentsByTypeBetweenDates(
            @PathVariable Long labourId,
            @PathVariable LabourPaymentType paymentType,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        List<LabourPaymentResponse> response =
                paymentService
                        .getPaymentsByTypeBetweenDates(
                                labourId,
                                paymentType,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(paymentService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}