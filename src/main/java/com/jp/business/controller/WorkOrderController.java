package com.jp.business.controller;

import com.jp.business.dto.WorkOrderRequest;
import com.jp.business.dto.WorkOrderResponse;
import com.jp.business.entity.WorkOrder;
import com.jp.business.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/work-orders")
@CrossOrigin(origins = "http://localhost:5173")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(
            WorkOrderService workOrderService
    ) {
        this.workOrderService = workOrderService;
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponse> createWorkOrder(
            @Valid @RequestBody WorkOrderRequest request
    ) {

        WorkOrder workOrder =
                workOrderService.createWorkOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        workOrderService.toResponse(workOrder)
                );
    }

    @GetMapping
    public ResponseEntity<List<WorkOrderResponse>>
    getAllWorkOrders() {

        List<WorkOrderResponse> response =
                workOrderService
                        .getAllWorkOrders()
                        .stream()
                        .map(workOrderService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderResponse>
    getWorkOrder(
            @PathVariable Long id
    ) {

        WorkOrder workOrder =
                workOrderService
                        .getWorkOrderById(id);

        return ResponseEntity.ok(
                workOrderService.toResponse(workOrder)
        );
    }

    @GetMapping("/quotation/{quotationId}")
    public ResponseEntity<List<WorkOrderResponse>>
    getWorkOrdersByQuotation(
            @PathVariable Long quotationId
    ) {

        List<WorkOrderResponse> response =
                workOrderService
                        .getWorkOrdersByQuotation(quotationId)
                        .stream()
                        .map(workOrderService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<WorkOrderResponse>>
    getWorkOrdersByStatus(
            @PathVariable String status
    ) {

        List<WorkOrderResponse> response =
                workOrderService
                        .getWorkOrdersByStatus(
                                status.toUpperCase()
                        )
                        .stream()
                        .map(workOrderService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WorkOrderResponse>
    updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {

        WorkOrder workOrder =
                workOrderService.updateStatus(
                        id,
                        status
                );

        return ResponseEntity.ok(
                workOrderService.toResponse(workOrder)
        );
    }
}