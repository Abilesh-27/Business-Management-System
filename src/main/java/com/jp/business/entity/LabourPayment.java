package com.jp.business.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "labour_payments")
public class LabourPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "labour_id", nullable = false)
    private Labour labour;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private LabourPaymentType paymentType;

    @Column(name = "salary_period_start")
    private LocalDate salaryPeriodStart;

    @Column(name = "salary_period_end")
    private LocalDate salaryPeriodEnd;

    @Column(columnDefinition = "TEXT")
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

        if (paymentDate == null) {
            paymentDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public LabourPayment() {
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}