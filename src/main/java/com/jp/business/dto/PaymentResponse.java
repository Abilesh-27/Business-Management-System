package com.jp.business.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;

    private String paymentNo;

    private Long invoiceId;

    private String invoiceNo;

    private Long workOrderId;

    private String workOrderNo;

    private Long quotationId;

private String quotationNo;

   private Long enquiryId;

private String enquiryNo;

private Long customerId;

private String customerName;
    private BigDecimal amount;

    private LocalDate paymentDate;

    private String paymentMethod;

    private String referenceNo;

    private String notes;

    private BigDecimal invoiceTotalAmount;

    private BigDecimal invoicePaidAmount;

    private BigDecimal invoiceBalanceAmount;

    private String invoiceStatus;

    private LocalDateTime createdAt;

    public PaymentResponse() {
    }
    public Long getQuotationId() {
    return quotationId;
}

public void setQuotationId(Long quotationId) {
    this.quotationId = quotationId;
}
public Long getEnquiryId() {
    return enquiryId;
}

public void setEnquiryId(Long enquiryId) {
    this.enquiryId = enquiryId;
}

public String getEnquiryNo() {
    return enquiryNo;
}

public void setEnquiryNo(String enquiryNo) {
    this.enquiryNo = enquiryNo;
}

public String getQuotationNo() {
    return quotationNo;
}

public void setQuotationNo(String quotationNo) {
    this.quotationNo = quotationNo;
}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNo() {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo) {
        this.workOrderNo = workOrderNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getInvoiceTotalAmount() {
        return invoiceTotalAmount;
    }

    public void setInvoiceTotalAmount(BigDecimal invoiceTotalAmount) {
        this.invoiceTotalAmount = invoiceTotalAmount;
    }

    public BigDecimal getInvoicePaidAmount() {
        return invoicePaidAmount;
    }

    public void setInvoicePaidAmount(BigDecimal invoicePaidAmount) {
        this.invoicePaidAmount = invoicePaidAmount;
    }

    public BigDecimal getInvoiceBalanceAmount() {
        return invoiceBalanceAmount;
    }

    public void setInvoiceBalanceAmount(BigDecimal invoiceBalanceAmount) {
        this.invoiceBalanceAmount = invoiceBalanceAmount;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}