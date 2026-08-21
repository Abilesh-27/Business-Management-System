package com.jp.business.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "labour_salary_settlements",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "labour_id",
                                "period_start",
                                "period_end"
                        }
                )
        }
)
public class LabourSalarySettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "labour_id", nullable = false)
    private Labour labour;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(
            name = "total_work_earnings",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal totalWorkEarnings = BigDecimal.ZERO;

    @Column(
            name = "salary_already_paid",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal salaryAlreadyPaid = BigDecimal.ZERO;

    @Column(
            name = "advance_recovered",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal advanceRecovered = BigDecimal.ZERO;

    @Column(
            name = "salary_due",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal salaryDue = BigDecimal.ZERO;

    @Column(
            name = "salary_remaining",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal salaryRemaining = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalarySettlementStatus status =
            SalarySettlementStatus.PENDING;

    private String notes;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (totalWorkEarnings == null) {
            totalWorkEarnings = BigDecimal.ZERO;
        }

        if (salaryAlreadyPaid == null) {
            salaryAlreadyPaid = BigDecimal.ZERO;
        }

        if (advanceRecovered == null) {
            advanceRecovered = BigDecimal.ZERO;
        }

        if (salaryDue == null) {
            salaryDue = BigDecimal.ZERO;
        }

        if (salaryRemaining == null) {
            salaryRemaining = BigDecimal.ZERO;
        }

        if (status == null) {
            status = SalarySettlementStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public LabourSalarySettlement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Labour getLabour() {
        return labour;
    }

    public void setLabour(Labour labour) {
        this.labour = labour;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}