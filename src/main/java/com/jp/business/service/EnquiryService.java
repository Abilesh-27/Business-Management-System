package com.jp.business.service;

import com.jp.business.dto.EnquiryRequest;
import com.jp.business.entity.Customer;
import com.jp.business.entity.Enquiry;
import com.jp.business.entity.EnquiryStatus;
import com.jp.business.repository.CustomerRepository;
import com.jp.business.repository.EnquiryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final CustomerRepository customerRepository;

    public EnquiryService(
            EnquiryRepository enquiryRepository,
            CustomerRepository customerRepository
    ) {
        this.enquiryRepository = enquiryRepository;
        this.customerRepository = customerRepository;
    }

    public Enquiry createEnquiry(EnquiryRequest request) {

        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found"
                        )
                );

        Enquiry enquiry = new Enquiry();

        enquiry.setEnquiryNo(
                generateEnquiryNo()
        );

        enquiry.setCustomer(customer);
        enquiry.setServiceType(
                request.getServiceType().trim()
        );
        enquiry.setDescription(
                request.getDescription()
        );
        enquiry.setSiteAddress(
                request.getSiteAddress()
        );
        enquiry.setCity(
                request.getCity()
        );
        enquiry.setExpectedWorkDate(
                request.getExpectedWorkDate()
        );
        enquiry.setFollowUpDate(
                request.getFollowUpDate()
        );
        enquiry.setSource(
                request.getSource()
        );
        enquiry.setNotes(
                request.getNotes()
        );

        enquiry.setStatus(EnquiryStatus.NEW);

        return enquiryRepository.save(enquiry);
    }

    private String generateEnquiryNo() {

        long nextNumber =
                enquiryRepository.count() + 1;

        return String.format(
                "ENQ-%04d",
                nextNumber
        );
    }

    public List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAll();
    }

    public Enquiry getEnquiryById(Long id) {

        return enquiryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Enquiry not found"
                        )
                );
    }

    public List<Enquiry> getCustomerEnquiries(
            Long customerId
    ) {

        return enquiryRepository
                .findByCustomerId(customerId);
    }

    public Enquiry updateStatus(
            Long id,
            EnquiryStatus status
    ) {

        Enquiry enquiry = getEnquiryById(id);

        enquiry.setStatus(status);

        return enquiryRepository.save(enquiry);
    }
}