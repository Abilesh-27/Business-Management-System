package com.jp.business.service;

import com.jp.business.dto.SiteVisitRequest;
import com.jp.business.entity.Customer;
import com.jp.business.entity.Enquiry;
import com.jp.business.entity.SiteVisit;
import com.jp.business.entity.SiteVisitStatus;
import com.jp.business.repository.EnquiryRepository;
import com.jp.business.repository.SiteVisitRepository;
import org.springframework.stereotype.Service;
import com.jp.business.dto.SiteVisitResponse;
import java.time.LocalDate;
import java.util.List;

@Service
public class SiteVisitService {

    private final SiteVisitRepository siteVisitRepository;
    private final EnquiryRepository enquiryRepository;

    public SiteVisitService(
            SiteVisitRepository siteVisitRepository,
            EnquiryRepository enquiryRepository
    ) {
        this.siteVisitRepository = siteVisitRepository;
        this.enquiryRepository = enquiryRepository;
    }

    public SiteVisit createSiteVisit(
            SiteVisitRequest request
    ) {

        Enquiry enquiry = enquiryRepository
                .findById(request.getEnquiryId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Enquiry not found"
                        )
                );

        SiteVisit siteVisit = new SiteVisit();

        siteVisit.setEnquiry(enquiry);
        siteVisit.setVisitDate(request.getVisitDate());
        siteVisit.setVisitTime(request.getVisitTime());
        siteVisit.setVisitedBy(request.getVisitedBy());
        siteVisit.setSiteAddress(request.getSiteAddress());
        siteVisit.setContactPerson(request.getContactPerson());
        siteVisit.setContactPhone(request.getContactPhone());
        siteVisit.setObservations(request.getObservations());
        siteVisit.setMeasurements(request.getMeasurements());
        siteVisit.setRequirements(request.getRequirements());
        siteVisit.setNotes(request.getNotes());
        siteVisit.setStatus(SiteVisitStatus.SCHEDULED);

        return siteVisitRepository.save(siteVisit);
    }

    public List<SiteVisit> getAllSiteVisits() {
        return siteVisitRepository.findAll();
    }

    public SiteVisit getSiteVisitById(Long id) {

        return siteVisitRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Site visit not found"
                        )
                );
    }

    public List<SiteVisit> getEnquirySiteVisits(
            Long enquiryId
    ) {

        return siteVisitRepository
                .findByEnquiryId(enquiryId);
    }

    public List<SiteVisit> getVisitsByDate(
            LocalDate date
    ) {

        return siteVisitRepository
                .findByVisitDate(date);
    }

    public SiteVisit updateStatus(
            Long id,
            SiteVisitStatus status
    ) {

        SiteVisit siteVisit =
                getSiteVisitById(id);

        siteVisit.setStatus(status);

        return siteVisitRepository.save(siteVisit);
    }
    public SiteVisitResponse toResponse(SiteVisit siteVisit) {

    SiteVisitResponse response = new SiteVisitResponse();

    response.setId(siteVisit.getId());

    Enquiry enquiry = siteVisit.getEnquiry();

    response.setEnquiryId(enquiry.getId());
    response.setEnquiryNo(enquiry.getEnquiryNo());

    Customer customer = enquiry.getCustomer();

    response.setCustomerId(customer.getId());
    response.setCustomerName(customer.getName());

    response.setVisitDate(siteVisit.getVisitDate());
    response.setVisitTime(siteVisit.getVisitTime());
    response.setVisitedBy(siteVisit.getVisitedBy());
    response.setSiteAddress(siteVisit.getSiteAddress());
    response.setContactPerson(siteVisit.getContactPerson());
    response.setContactPhone(siteVisit.getContactPhone());
    response.setObservations(siteVisit.getObservations());
    response.setMeasurements(siteVisit.getMeasurements());
    response.setRequirements(siteVisit.getRequirements());
    response.setNotes(siteVisit.getNotes());
    response.setStatus(siteVisit.getStatus());
    response.setCreatedAt(siteVisit.getCreatedAt());
    response.setUpdatedAt(siteVisit.getUpdatedAt());

    return response;
}
}