package com.jp.business.repository;

import com.jp.business.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuotationRepository
        extends JpaRepository<Quotation, Long> {

    Optional<Quotation> findByQuotationNo(String quotationNo);

    List<Quotation> findByEnquiryId(Long enquiryId);

    List<Quotation> findByStatus(String status);
}