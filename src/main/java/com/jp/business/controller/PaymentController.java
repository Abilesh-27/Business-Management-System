package com.jp.business.controller;

import com.jp.business.dto.PaymentRequest;
import com.jp.business.dto.PaymentResponse;
import com.jp.business.entity.Payment;
import com.jp.business.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request
    ) {

        Payment payment =
                paymentService.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        paymentService.toResponse(payment)
                );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>>
    getAllPayments() {

        List<PaymentResponse> response =
                paymentService
                        .getAllPayments()
                        .stream()
                        .map(paymentService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse>
    getPayment(
            @PathVariable Long id
    ) {

        Payment payment =
                paymentService.getPaymentById(id);

        return ResponseEntity.ok(
                paymentService.toResponse(payment)
        );
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentResponse>>
    getPaymentsByInvoice(
            @PathVariable Long invoiceId
    ) {

        List<PaymentResponse> response =
                paymentService
                        .getPaymentsByInvoice(invoiceId)
                        .stream()
                        .map(paymentService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}