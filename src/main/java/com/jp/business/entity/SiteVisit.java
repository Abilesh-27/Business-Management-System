package com.jp.business.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "site_visits")
public class SiteVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enquiry_id", nullable = false)
    private Enquiry enquiry;

    @Column(nullable = false)
    private LocalDate visitDate;

    @Column(nullable = false)
    private LocalTime visitTime;

    private String visitedBy;

    private String siteAddress;

    private String contactPerson;

    private String contactPhone;

    @Column(length = 2000)
    private String observations;

    @Column(length = 2000)
    private String measurements;

    @Column(length = 2000)
    private String requirements;

    @Column(length = 2000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SiteVisitStatus status = SiteVisitStatus.SCHEDULED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = SiteVisitStatus.SCHEDULED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public SiteVisit() {
    }

    public Long getId() {
        return id;
    }

    public Enquiry getEnquiry() {
        return enquiry;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public LocalTime getVisitTime() {
        return visitTime;
    }

    public String getVisitedBy() {
        return visitedBy;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getObservations() {
        return observations;
    }

    public String getMeasurements() {
        return measurements;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getNotes() {
        return notes;
    }

    public SiteVisitStatus getStatus() {
        return status;
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

    public void setEnquiry(Enquiry enquiry) {
        this.enquiry = enquiry;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public void setVisitTime(LocalTime visitTime) {
        this.visitTime = visitTime;
    }

    public void setVisitedBy(String visitedBy) {
        this.visitedBy = visitedBy;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setMeasurements(String measurements) {
        this.measurements = measurements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(SiteVisitStatus status) {
        this.status = status;
    }
}