package com.jp.business.dto;

import java.math.BigDecimal;

public class LabourAdvanceSummaryResponse {

    private Long labourId;
    private String labourNo;
    private String labourName;

    private BigDecimal totalAdvanceGiven;
    private BigDecimal totalAdvanceRecovered;
    private BigDecimal outstandingAdvance;

    public LabourAdvanceSummaryResponse() {
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
}