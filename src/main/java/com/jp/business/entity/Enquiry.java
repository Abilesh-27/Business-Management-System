package com.jp.business.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "enquiries",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "enquiryNo")
        }
)
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String enquiryNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String serviceType;

    @Column(length = 2000)
    private String description;

    private String siteAddress;

    private String city;

    private LocalDate expectedWorkDate;

    private LocalDate followUpDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnquiryStatus status = EnquiryStatus.NEW;

    private String source;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = EnquiryStatus.NEW;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Enquiry() {
    }

    public Long getId() {
        return id;
    }

    public String getEnquiryNo() {
        return enquiryNo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getDescription() {
        return description;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getExpectedWorkDate() {
        return expectedWorkDate;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public EnquiryStatus getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnquiryNo(String enquiryNo) {
        this.enquiryNo = enquiryNo;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setExpectedWorkDate(LocalDate expectedWorkDate) {
        this.expectedWorkDate = expectedWorkDate;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public void setStatus(EnquiryStatus status) {
        this.status = status;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}