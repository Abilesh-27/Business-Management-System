package com.jp.business.dto;

import com.jp.business.entity.LabourPaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LabourPaymentResponse {

    private Long id;

    private Long labourId;
    private String labourNo;
    private String labourName;

    private LocalDate paymentDate;

    private BigDecimal amount;

    private LabourPaymentType paymentType;

    private LocalDate salaryPeriodStart;

    private LocalDate salaryPeriodEnd;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public LabourPaymentResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLabourId() {
        return labourId;
    }

    public void setLabourId(Long labourId) {
        this.labourId = labourId;
    }

    public String getLabourNo() {
        return labourNo;
    }

    public void setLabourNo(String labourNo) {
        this.labourNo = labourNo;
    }

    public String getLabourName() {
        return labourName;
    }

    public void setLabourName(String labourName) {
        this.labourName = labourName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            LocalDateTime updatedAt
    ) {
        this.updatedAt = updatedAt;
    }
}