package com.jp.business.service;

import com.jp.business.dto.InvoiceRequest;
import com.jp.business.dto.InvoiceResponse;
import com.jp.business.entity.Customer;
import com.jp.business.entity.Enquiry;
import com.jp.business.entity.Invoice;
import com.jp.business.entity.Quotation;
import com.jp.business.entity.WorkOrder;
import com.jp.business.repository.InvoiceRepository;
import com.jp.business.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final WorkOrderRepository workOrderRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            WorkOrderRepository workOrderRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.workOrderRepository = workOrderRepository;
    }

    @Transactional
    public Invoice createInvoice(
            InvoiceRequest request
    ) {

        WorkOrder workOrder =
                workOrderRepository
                        .findById(request.getWorkOrderId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Work Order not found: "
                                                + request.getWorkOrderId()
                                )
                        );

        if (!"COMPLETED".equalsIgnoreCase(
                workOrder.getStatus()
        )) {

            throw new RuntimeException(
                    "Invoice can only be created "
                            + "for a COMPLETED work order"
            );
        }

        BigDecimal subtotal =
                request.getSubtotal() != null
                        ? request.getSubtotal()
                        : workOrder.getAmount();

        BigDecimal discount =
                request.getDiscount() != null
                        ? request.getDiscount()
                        : BigDecimal.ZERO;

        BigDecimal tax =
                request.getTax() != null
                        ? request.getTax()
                        : BigDecimal.ZERO;

        BigDecimal totalAmount =
                subtotal
                        .subtract(discount)
                        .add(tax);

        if (totalAmount.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            throw new RuntimeException(
                    "Invoice total cannot be negative"
            );
        }

        Invoice invoice = new Invoice();

        invoice.setInvoiceNo(
                generateInvoiceNumber()
        );

        invoice.setWorkOrder(workOrder);

        invoice.setInvoiceDate(
                request.getInvoiceDate() != null
                        ? request.getInvoiceDate()
                        : LocalDate.now()
        );

        invoice.setDueDate(
                request.getDueDate()
        );

        invoice.setSubtotal(subtotal);

        invoice.setDiscount(discount);

        invoice.setTax(tax);

        invoice.setTotalAmount(totalAmount);

        invoice.setPaidAmount(
                BigDecimal.ZERO
        );

        invoice.setBalanceAmount(
                totalAmount
        );

        invoice.setStatus("UNPAID");

        invoice.setNotes(
                request.getNotes()
        );

        return invoiceRepository.save(invoice);
    }

    private String generateInvoiceNumber() {

        long count =
                invoiceRepository.count() + 1;

        return String.format(
                "INV-%04d",
                count
        );
    }

    @Transactional(readOnly = true)
    public List<Invoice> getAllInvoices() {

        return invoiceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Invoice getInvoiceById(
            Long id
    ) {

        return invoiceRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invoice not found: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByWorkOrder(
            Long workOrderId
    ) {

        return invoiceRepository
                .findByWorkOrderId(workOrderId);
    }

    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByStatus(
            String status
    ) {

        return invoiceRepository
                .findByStatus(status.toUpperCase());
    }

    @Transactional(readOnly = true)
    public InvoiceResponse toResponse(
            Invoice invoice
    ) {

        InvoiceResponse response =
                new InvoiceResponse();

        response.setId(
                invoice.getId()
        );

        response.setInvoiceNo(
                invoice.getInvoiceNo()
        );

        WorkOrder workOrder =
                invoice.getWorkOrder();

        response.setWorkOrderId(
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

        response.setInvoiceDate(
                invoice.getInvoiceDate()
        );

        response.setDueDate(
                invoice.getDueDate()
        );

        response.setSubtotal(
                invoice.getSubtotal()
        );

        response.setDiscount(
                invoice.getDiscount()
        );

        response.setTax(
                invoice.getTax()
        );

        response.setTotalAmount(
                invoice.getTotalAmount()
        );

        response.setPaidAmount(
                invoice.getPaidAmount()
        );

        response.setBalanceAmount(
                invoice.getBalanceAmount()
        );

        response.setStatus(
                invoice.getStatus()
        );

        response.setNotes(
                invoice.getNotes()
        );

        response.setCreatedAt(
                invoice.getCreatedAt()
        );

        response.setUpdatedAt(
                invoice.getUpdatedAt()
        );

        return response;
    }
}