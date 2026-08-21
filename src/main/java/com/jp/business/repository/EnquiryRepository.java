package com.jp.business.repository;

import com.jp.business.entity.Enquiry;
import com.jp.business.entity.EnquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnquiryRepository
        extends JpaRepository<Enquiry, Long> {

    Optional<Enquiry> findByEnquiryNo(String enquiryNo);

    List<Enquiry> findByCustomerId(Long customerId);

    List<Enquiry> findByStatus(EnquiryStatus status);
}