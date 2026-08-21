package com.jp.business.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LabourFinancialReportResponse {

    private Long labourId;
    private String labourNo;
    private String labourName;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal dailyWage;
    private BigDecimal hourlyWage;

    private BigDecimal totalWorkingHours;
    private BigDecimal totalNormalHours;
    private BigDecimal totalOvertimeHours;

    private BigDecimal totalWorkEarnings;

    private BigDecimal totalSalaryPaid;

    private BigDecimal totalAdvanceGiven;
    private BigDecimal totalAdvanceRecovered;
    private BigDecimal outstandingAdvance;

    private BigDecimal pendingSalary;

    private BigDecimal totalMoneyGiven;

    public LabourFinancialReportResponse() {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getDailyWage() {
        return dailyWage;
    }

    public void setDailyWage(BigDecimal dailyWage) {
        this.dailyWage = dailyWage;
    }

    public BigDecimal getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(BigDecimal hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public BigDecimal getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public void setTotalWorkingHours(
            BigDecimal totalWorkingHours
    ) {
        this.totalWorkingHours = totalWorkingHours;
    }

    public BigDecimal getTotalNormalHours() {
        return totalNormalHours;
    }

    public void setTotalNormalHours(
            BigDecimal totalNormalHours
    ) {
        this.totalNormalHours = totalNormalHours;
    }

    public BigDecimal getTotalOvertimeHours() {
        return totalOvertimeHours;
    }

    public void setTotalOvertimeHours(
            BigDecimal totalOvertimeHours
    ) {
        this.totalOvertimeHours = totalOvertimeHours;
    }

    public BigDecimal getTotalWorkEarnings() {
        return totalWorkEarnings;
    }

    public void setTotalWorkEarnings(
            BigDecimal totalWorkEarnings
    ) {
        this.totalWorkEarnings = totalWorkEarnings;
    }

    public BigDecimal getTotalSalaryPaid() {
        return totalSalaryPaid;
    }

    public void setTotalSalaryPaid(
            BigDecimal totalSalaryPaid
    ) {
        this.totalSalaryPaid = totalSalaryPaid;
    }

    public BigDecimal getTotalAdvanceGiven() {
        return totalAdvanceGiven;
    }

    public void setTotalAdvanceGiven(
            BigDecimal totalAdvanceGiven
    ) {
        this.totalAdvanceGiven = totalAdvanceGiven;
    }

    public BigDecimal getTotalAdvanceRecovered() {
        return totalAdvanceRecovered;
    }

    public void setTotalAdvanceRecovered(
            BigDecimal totalAdvanceRecovered
    ) {
        this.totalAdvanceRecovered =
                totalAdvanceRecovered;
    }

    public BigDecimal getOutstandingAdvance() {
        return outstandingAdvance;
    }

    public void setOutstandingAdvance(
            BigDecimal outstandingAdvance
    ) {
        this.outstandingAdvance =
                outstandingAdvance;
    }

    public BigDecimal getPendingSalary() {
        return pendingSalary;
    }

    public void setPendingSalary(
            BigDecimal pendingSalary
    ) {
        this.pendingSalary = pendingSalary;
    }

    public BigDecimal getTotalMoneyGiven() {
        return totalMoneyGiven;
    }

    public void setTotalMoneyGiven(
            BigDecimal totalMoneyGiven
    ) {
        this.totalMoneyGiven = totalMoneyGiven;
    }
}