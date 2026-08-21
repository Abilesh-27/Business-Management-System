package com.jp.business.repository;

import com.jp.business.entity.SiteVisit;
import com.jp.business.entity.SiteVisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SiteVisitRepository
        extends JpaRepository<SiteVisit, Long> {

    List<SiteVisit> findByEnquiryId(Long enquiryId);

    List<SiteVisit> findByStatus(SiteVisitStatus status);

    List<SiteVisit> findByVisitDate(LocalDate visitDate);
}