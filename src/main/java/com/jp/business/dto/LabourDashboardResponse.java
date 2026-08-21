package com.jp.business.dto;

import java.math.BigDecimal;

public class LabourDashboardResponse {

    private long totalLabours;
    private long workingToday;
    private long overtimeWorkers;

    private BigDecimal totalWorkingHours;
    private BigDecimal totalOvertimeHours;
    private BigDecimal todayLabourCost;

    private BigDecimal pendingSalary;
    private BigDecimal outstandingAdvance;

    public LabourDashboardResponse() {
    }

    public long getTotalLabours() {
        return totalLabours;
    }

    public void setTotalLabours(long totalLabours) {
        this.totalLabours = totalLabours;
    }

    public long getWorkingToday() {
        return workingToday;
    }

    public void setWorkingToday(long workingToday) {
        this.workingToday = workingToday;
    }

    public long getOvertimeWorkers() {
        return overtimeWorkers;
    }

    public void setOvertimeWorkers(long overtimeWorkers) {
        this.overtimeWorkers = overtimeWorkers;
    }

    public BigDecimal getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public void setTotalWorkingHours(
            BigDecimal totalWorkingHours
    ) {
        this.totalWorkingHours = totalWorkingHours;
    }

    public BigDecimal getTotalOvertimeHours() {
        return totalOvertimeHours;
    }

    public void setTotalOvertimeHours(
            BigDecimal totalOvertimeHours
    ) {
        this.totalOvertimeHours = totalOvertimeHours;
    }

    public BigDecimal getTodayLabourCost() {
        return todayLabourCost;
    }

    public void setTodayLabourCost(
            BigDecimal todayLabourCost
    ) {
        this.todayLabourCost = todayLabourCost;
    }

    public BigDecimal getPendingSalary() {
        return pendingSalary;
    }

    public void setPendingSalary(
            BigDecimal pendingSalary
    ) {
        this.pendingSalary = pendingSalary;
    }

    public BigDecimal getOutstandingAdvance() {
        return outstandingAdvance;
    }

    public void setOutstandingAdvance(
            BigDecimal outstandingAdvance
    ) {
        this.outstandingAdvance = outstandingAdvance;
    }
}