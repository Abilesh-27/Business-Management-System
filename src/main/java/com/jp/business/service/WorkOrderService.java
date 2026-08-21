package com.jp.business.service;

import com.jp.business.dto.WorkOrderRequest;
import com.jp.business.dto.WorkOrderResponse;
import com.jp.business.entity.Customer;
import com.jp.business.entity.Enquiry;
import com.jp.business.entity.Quotation;
import com.jp.business.entity.WorkOrder;
import com.jp.business.repository.QuotationRepository;
import com.jp.business.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final QuotationRepository quotationRepository;

    public WorkOrderService(
            WorkOrderRepository workOrderRepository,
            QuotationRepository quotationRepository
    ) {
        this.workOrderRepository = workOrderRepository;
        this.quotationRepository = quotationRepository;
    }

    @Transactional
    public WorkOrder createWorkOrder(
            WorkOrderRequest request
    ) {

        Quotation quotation =
                quotationRepository
                        .findById(request.getQuotationId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Quotation not found: "
                                                + request.getQuotationId()
                                )
                        );

        if (!"ACCEPTED".equalsIgnoreCase(
                quotation.getStatus()
        )) {

            throw new RuntimeException(
                    "Work Order can only be created "
                            + "for an ACCEPTED quotation"
            );
        }

        BigDecimal amount =
                request.getAmount() != null
                        ? request.getAmount()
                        : quotation.getTotalAmount();

        WorkOrder workOrder = new WorkOrder();

        workOrder.setWorkOrderNo(
                generateWorkOrderNumber()
        );

        workOrder.setQuotation(quotation);

        workOrder.setStartDate(
                request.getStartDate()
        );

        workOrder.setExpectedCompletionDate(
                request.getExpectedCompletionDate()
        );

        workOrder.setAssignedTo(
                request.getAssignedTo()
        );

        workOrder.setWorkDescription(
                request.getWorkDescription()
        );

        workOrder.setAmount(amount);

        workOrder.setStatus("PENDING");

        workOrder.setNotes(
                request.getNotes()
        );

        return workOrderRepository.save(workOrder);
    }

    private String generateWorkOrderNumber() {

        long count =
                workOrderRepository.count() + 1;

        return String.format(
                "WO-%04d",
                count
        );
    }

    @Transactional(readOnly = true)
    public List<WorkOrder> getAllWorkOrders() {

        return workOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public WorkOrder getWorkOrderById(
            Long id
    ) {

        return workOrderRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Work Order not found: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersByQuotation(
            Long quotationId
    ) {

        return workOrderRepository
                .findByQuotationId(quotationId);
    }

    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersByStatus(
            String status
    ) {

        return workOrderRepository
                .findByStatus(status);
    }

    @Transactional
    public WorkOrder updateStatus(
            Long id,
            String status
    ) {

        WorkOrder workOrder =
                getWorkOrderById(id);

        workOrder.setStatus(
                status.toUpperCase()
        );

        return workOrderRepository.save(
                workOrder
        );
    }

    @Transactional(readOnly = true)
    public WorkOrderResponse toResponse(
            WorkOrder workOrder
    ) {

        WorkOrderResponse response =
                new WorkOrderResponse();

        response.setId(
                workOrder.getId()
        );

        response.setWorkOrderNo(
                workOrder.getWorkOrderNo()
        );

        Quotation quotation =
                workOrder.getQuotation();

        response.setQuotationId(
                quotation.getId()
        );

        response.setQuotationNo(
                quotation.getQuotationNo()
        );

        Enquiry enquiry =
                quotation.getEnquiry();

        response.setEnquiryId(
                enquiry.getId()
        );

        response.setEnquiryNo(
                enquiry.getEnquiryNo()
        );

        Customer customer =
                enquiry.getCustomer();

        response.setCustomerId(
                customer.getId()
        );

        response.setCustomerName(
                customer.getName()
        );

        response.setStartDate(
                workOrder.getStartDate()
        );

        response.setExpectedCompletionDate(
                workOrder.getExpectedCompletionDate()
        );

        response.setAssignedTo(
                workOrder.getAssignedTo()
        );

        response.setWorkDescription(
                workOrder.getWorkDescription()
        );

        response.setAmount(
                workOrder.getAmount()
        );

        response.setStatus(
                workOrder.getStatus()
        );

        response.setNotes(
                workOrder.getNotes()
        );

        response.setCreatedAt(
                workOrder.getCreatedAt()
        );

        response.setUpdatedAt(
                workOrder.getUpdatedAt()
        );

        return response;
    }
}