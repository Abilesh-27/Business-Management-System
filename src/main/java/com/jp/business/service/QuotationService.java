package com.jp.business.service;

import com.jp.business.dto.QuotationRequest;
import com.jp.business.dto.QuotationResponse;
import com.jp.business.entity.Customer;
import com.jp.business.entity.Enquiry;
import com.jp.business.entity.Quotation;
import com.jp.business.entity.SiteVisit;
import com.jp.business.repository.EnquiryRepository;
import com.jp.business.repository.QuotationRepository;
import com.jp.business.repository.SiteVisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final EnquiryRepository enquiryRepository;
    private final SiteVisitRepository siteVisitRepository;

    public QuotationService(
            QuotationRepository quotationRepository,
            EnquiryRepository enquiryRepository,
            SiteVisitRepository siteVisitRepository
    ) {
        this.quotationRepository = quotationRepository;
        this.enquiryRepository = enquiryRepository;
        this.siteVisitRepository = siteVisitRepository;
    }

    @Transactional
    public Quotation createQuotation(QuotationRequest request) {

        Enquiry enquiry = enquiryRepository
                .findById(request.getEnquiryId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Enquiry not found: " +
                                request.getEnquiryId()
                        )
                );

        SiteVisit siteVisit = null;

        if (request.getSiteVisitId() != null) {

            siteVisit = siteVisitRepository
                    .findById(request.getSiteVisitId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Site Visit not found: " +
                                    request.getSiteVisitId()
                            )
                    );
        }

        BigDecimal subtotal =
                request.getSubtotal() != null
                        ? request.getSubtotal()
                        : BigDecimal.ZERO;

        BigDecimal discount =
                request.getDiscount() != null
                        ? request.getDiscount()
                        : BigDecimal.ZERO;

        BigDecimal tax =
                request.getTax() != null
                        ? request.getTax()
                        : BigDecimal.ZERO;

        BigDecimal totalAmount =
                subtotal
                        .subtract(discount)
                        .add(tax);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                    "Total amount cannot be negative"
            );
        }

        Quotation quotation = new Quotation();

        quotation.setQuotationNo(generateQuotationNumber());

        quotation.setEnquiry(enquiry);
        quotation.setSiteVisit(siteVisit);

        quotation.setQuotationDate(
                request.getQuotationDate() != null
                        ? request.getQuotationDate()
                        : LocalDate.now()
        );

        quotation.setValidUntil(
                request.getValidUntil()
        );

        quotation.setSubtotal(subtotal);
        quotation.setDiscount(discount);
        quotation.setTax(tax);
        quotation.setTotalAmount(totalAmount);

        quotation.setTerms(request.getTerms());
        quotation.setNotes(request.getNotes());

        quotation.setStatus("DRAFT");

        return quotationRepository.save(quotation);
    }

    private String generateQuotationNumber() {

        long count =
                quotationRepository.count() + 1;

        return String.format(
                "QUO-%04d",
                count
        );
    }

    @Transactional(readOnly = true)
    public List<Quotation> getAllQuotations() {

        return quotationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Quotation getQuotationById(Long id) {

        return quotationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Quotation not found: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Quotation> getQuotationsByEnquiry(
            Long enquiryId
    ) {

        return quotationRepository
                .findByEnquiryId(enquiryId);
    }

    @Transactional(readOnly = true)
    public List<Quotation> getQuotationsByStatus(
            String status
    ) {

        return quotationRepository
                .findByStatus(status);
    }

    @Transactional
    public Quotation updateStatus(
            Long id,
            String status
    ) {

        Quotation quotation =
                getQuotationById(id);

        quotation.setStatus(status.toUpperCase());

        return quotationRepository.save(quotation);
    }

    @Transactional(readOnly = true)
    public QuotationResponse toResponse(
            Quotation quotation
    ) {

        QuotationResponse response =
                new QuotationResponse();

        response.setId(quotation.getId());

        response.setQuotationNo(
                quotation.getQuotationNo()
        );

        Enquiry enquiry =
                quotation.getEnquiry();

        response.setEnquiryId(
                enquiry.getId()
        );

        response.setEnquiryNo(
                enquiry.getEnquiryNo()
        );

        Customer customer =
                enquiry.getCustomer();

        response.setCustomerId(
                customer.getId()
        );

        response.setCustomerName(
                customer.getName()
        );

        if (quotation.getSiteVisit() != null) {

            response.setSiteVisitId(
                    quotation.getSiteVisit().getId()
            );
        }

        response.setQuotationDate(
                quotation.getQuotationDate()
        );

        response.setValidUntil(
                quotation.getValidUntil()
        );

        response.setSubtotal(
                quotation.getSubtotal()
        );

        response.setDiscount(
                quotation.getDiscount()
        );

        response.setTax(
                quotation.getTax()
        );

        response.setTotalAmount(
                quotation.getTotalAmount()
        );

        response.setTerms(
                quotation.getTerms()
        );

        response.setNotes(
                quotation.getNotes()
        );

        response.setStatus(
                quotation.getStatus()
        );

        response.setCreatedAt(
                quotation.getCreatedAt()
        );

        response.setUpdatedAt(
                quotation.getUpdatedAt()
        );

        return response;
    }
}