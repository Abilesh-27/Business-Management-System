package com.jp.business.dto;

import com.jp.business.entity.SalarySettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LabourSalarySettlementResponse {

    private Long id;

    private Long labourId;
    private String labourNo;
    private String labourName;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    private BigDecimal totalWorkEarnings;
    private BigDecimal salaryAlreadyPaid;
    private BigDecimal advanceRecovered;

    private BigDecimal salaryDue;
    private BigDecimal salaryRemaining;

    private SalarySettlementStatus status;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LabourSalarySettlementResponse() {
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

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public BigDecimal getTotalWorkEarnings() {
        return totalWorkEarnings;
    }

    public void setTotalWorkEarnings(
            BigDecimal totalWorkEarnings
    ) {
        this.totalWorkEarnings = totalWorkEarnings;
    }

    public BigDecimal getSalaryAlreadyPaid() {
        return salaryAlreadyPaid;
    }

    public void setSalaryAlreadyPaid(
            BigDecimal salaryAlreadyPaid
    ) {
        this.salaryAlreadyPaid = salaryAlreadyPaid;
    }

    public BigDecimal getAdvanceRecovered() {
        return advanceRecovered;
    }

    public void setAdvanceRecovered(
            BigDecimal advanceRecovered
    ) {
        this.advanceRecovered = advanceRecovered;
    }

    public BigDecimal getSalaryDue() {
        return salaryDue;
    }

    public void setSalaryDue(BigDecimal salaryDue) {
        this.salaryDue = salaryDue;
    }

    public BigDecimal getSalaryRemaining() {
        return salaryRemaining;
    }

    public void setSalaryRemaining(
            BigDecimal salaryRemaining
    ) {
        this.salaryRemaining = salaryRemaining;
    }

    public SalarySettlementStatus getStatus() {
        return status;
    }

    public void setStatus(
            SalarySettlementStatus status
    ) {
        this.status = status;
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