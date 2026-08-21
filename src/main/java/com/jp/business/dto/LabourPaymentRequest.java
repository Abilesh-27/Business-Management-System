package com.jp.business.dto;

import com.jp.business.entity.LabourPaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LabourPaymentRequest {

    @NotNull(message = "Labour is required")
    private Long labourId;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Payment amount must be greater than zero"
    )
    private BigDecimal amount;

    @NotNull(message = "Payment type is required")
    private LabourPaymentType paymentType;

    private LocalDate salaryPeriodStart;

    private LocalDate salaryPeriodEnd;

    private String notes;

    public LabourPaymentRequest() {
    }

    public Long getLabourId() {
        return labourId;
    }

    public void setLabourId(Long labourId) {
        this.labourId = labourId;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LabourPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(
            LabourPaymentType paymentType
    ) {
        this.paymentType = paymentType;
    }

    public LocalDate getSalaryPeriodStart() {
        return salaryPeriodStart;
    }

    public void setSalaryPeriodStart(
            LocalDate salaryPeriodStart
    ) {
        this.salaryPeriodStart = salaryPeriodStart;
    }

    public LocalDate getSalaryPeriodEnd() {
        return salaryPeriodEnd;
    }

    public void setSalaryPeriodEnd(
            LocalDate salaryPeriodEnd
    ) {
        this.salaryPeriodEnd = salaryPeriodEnd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}