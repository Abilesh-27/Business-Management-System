package com.jp.business.controller;

import com.jp.business.dto.InvoiceRequest;
import com.jp.business.dto.InvoiceResponse;
import com.jp.business.entity.Invoice;
import com.jp.business.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:5173")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(
            InvoiceService invoiceService
    ) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(
            @Valid @RequestBody InvoiceRequest request
    ) {

        Invoice invoice =
                invoiceService.createInvoice(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        invoiceService.toResponse(invoice)
                );
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>>
    getAllInvoices() {

        List<InvoiceResponse> response =
                invoiceService
                        .getAllInvoices()
                        .stream()
                        .map(invoiceService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse>
    getInvoice(
            @PathVariable Long id
    ) {

        Invoice invoice =
                invoiceService.getInvoiceById(id);

        return ResponseEntity.ok(
                invoiceService.toResponse(invoice)
        );
    }

    @GetMapping("/work-order/{workOrderId}")
    public ResponseEntity<List<InvoiceResponse>>
    getInvoicesByWorkOrder(
            @PathVariable Long workOrderId
    ) {

        List<InvoiceResponse> response =
                invoiceService
                        .getInvoicesByWorkOrder(workOrderId)
                        .stream()
                        .map(invoiceService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InvoiceResponse>>
    getInvoicesByStatus(
            @PathVariable String status
    ) {

        List<InvoiceResponse> response =
                invoiceService
                        .getInvoicesByStatus(status)
                        .stream()
                        .map(invoiceService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}