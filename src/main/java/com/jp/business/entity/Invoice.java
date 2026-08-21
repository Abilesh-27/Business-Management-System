package com.jp.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "invoice_no",
            nullable = false,
            unique = true
    )
    private String invoiceNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "work_order_id",
            nullable = false
    )
    @JsonIgnoreProperties({
            "quotation",
            "hibernateLazyInitializer",
            "handler"
    })
    private WorkOrder workOrder;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal subtotal;

    @Column(
            precision = 12,
            scale = 2
    )
    private BigDecimal discount;

    @Column(
            precision = 12,
            scale = 2
    )
    private BigDecimal tax;

    @Column(
            name = "total_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal totalAmount;

    @Column(
            name = "paid_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal paidAmount;

    @Column(
            name = "balance_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal balanceAmount;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (invoiceDate == null) {
            invoiceDate = LocalDate.now();
        }

        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }

        if (discount == null) {
            discount = BigDecimal.ZERO;
        }

        if (tax == null) {
            tax = BigDecimal.ZERO;
        }

        if (totalAmount == null) {
            totalAmount = subtotal
                    .subtract(discount)
                    .add(tax);
        }

        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }

        if (balanceAmount == null) {
            balanceAmount = totalAmount
                    .subtract(paidAmount);
        }

        if (status == null) {
            status = "UNPAID";
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();

        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }

        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }

        balanceAmount = totalAmount
                .subtract(paidAmount);

        if (balanceAmount.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            balanceAmount = BigDecimal.ZERO;
            status = "PAID";

        } else if (paidAmount.compareTo(
                BigDecimal.ZERO
        ) > 0) {

            status = "PARTIALLY_PAID";

        } else {

            status = "UNPAID";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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