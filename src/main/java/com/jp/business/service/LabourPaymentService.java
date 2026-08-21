package com.jp.business.service;

import com.jp.business.dto.LabourPaymentRequest;
import com.jp.business.dto.LabourPaymentResponse;
import com.jp.business.entity.Labour;
import com.jp.business.entity.LabourPayment;
import com.jp.business.entity.LabourPaymentType;
import com.jp.business.repository.LabourPaymentRepository;
import com.jp.business.repository.LabourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LabourPaymentService {

    private final LabourPaymentRepository paymentRepository;
    private final LabourRepository labourRepository;

    public LabourPaymentService(
            LabourPaymentRepository paymentRepository,
            LabourRepository labourRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.labourRepository = labourRepository;
    }

    @Transactional
    public LabourPayment createPayment(
            LabourPaymentRequest request
    ) {

        Labour labour =
                labourRepository.findById(
                        request.getLabourId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Labour not found: "
                                        + request.getLabourId()
                        )
                );

        if (!labour.isActive()) {
            throw new RuntimeException(
                    "Cannot create payment for inactive labour"
            );
        }

        if (request.getAmount() == null
                || request.getAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Payment amount must be greater than zero"
            );
        }

        if (request.getPaymentDate() == null) {
            throw new RuntimeException(
                    "Payment date is required"
            );
        }

        if (request.getPaymentType() == null) {
            throw new RuntimeException(
                    "Payment type is required"
            );
        }

        if (request.getSalaryPeriodStart() != null
                && request.getSalaryPeriodEnd() != null
                && request.getSalaryPeriodStart()
                .isAfter(request.getSalaryPeriodEnd())) {

            throw new RuntimeException(
                    "Salary period start cannot be after end"
            );
        }

        /*
         * Salary period is meaningful mainly for salary payments.
         * Advances and advance recoveries can exist independently.
         */
        if (request.getPaymentType()
                != LabourPaymentType.SALARY) {

            if (request.getSalaryPeriodStart() != null
                    || request.getSalaryPeriodEnd() != null) {

                throw new RuntimeException(
                        "Salary period should only be provided for salary payments"
                );
            }
        }

        LabourPayment payment =
                new LabourPayment();

        payment.setLabour(labour);

        payment.setPaymentDate(
                request.getPaymentDate()
        );

        payment.setAmount(
                request.getAmount()
        );

        payment.setPaymentType(
                request.getPaymentType()
        );

        payment.setSalaryPeriodStart(
                request.getSalaryPeriodStart()
        );

        payment.setSalaryPeriodEnd(
                request.getSalaryPeriodEnd()
        );

        payment.setNotes(
                request.getNotes()
        );

        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public LabourPayment getPaymentById(
            Long id
    ) {

        return paymentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<LabourPayment> getAllPayments() {

        return paymentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<LabourPayment>
    getLabourPaymentHistory(
            Long labourId
    ) {

        if (!labourRepository.existsById(labourId)) {
            throw new RuntimeException(
                    "Labour not found: " + labourId
            );
        }

        return paymentRepository
                .findByLabourIdOrderByPaymentDateDesc(
                        labourId
                );
    }

    @Transactional(readOnly = true)
    public List<LabourPayment>
    getPaymentsBetweenDates(
            Long labourId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (!labourRepository.existsById(labourId)) {
            throw new RuntimeException(
                    "Labour not found: " + labourId
            );
        }

        if (startDate == null || endDate == null) {
            throw new RuntimeException(
                    "Start date and end date are required"
            );
        }

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException(
                    "Start date cannot be after end date"
            );
        }

        return paymentRepository
                .findByLabourIdAndPaymentDateBetweenOrderByPaymentDateAsc(
                        labourId,
                        startDate,
                        endDate
                );
    }

    @Transactional(readOnly = true)
    public List<LabourPayment>
    getPaymentsByType(
            Long labourId,
            LabourPaymentType paymentType
    ) {

        if (!labourRepository.existsById(labourId)) {
            throw new RuntimeException(
                    "Labour not found: " + labourId
            );
        }

        return paymentRepository
                .findByLabourIdAndPaymentTypeOrderByPaymentDateAsc(
                        labourId,
                        paymentType
                );
    }

    @Transactional(readOnly = true)
    public List<LabourPayment>
    getPaymentsByTypeBetweenDates(
            Long labourId,
            LabourPaymentType paymentType,
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (!labourRepository.existsById(labourId)) {
            throw new RuntimeException(
                    "Labour not found: " + labourId
            );
        }

        if (paymentType == null) {
            throw new RuntimeException(
                    "Payment type is required"
            );
        }

        if (startDate == null || endDate == null) {
            throw new RuntimeException(
                    "Start date and end date are required"
            );
        }

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException(
                    "Start date cannot be after end date"
            );
        }

        return paymentRepository
                .findByLabourIdAndPaymentTypeAndPaymentDateBetweenOrderByPaymentDateAsc(
                        labourId,
                        paymentType,
                        startDate,
                        endDate
                );
    }

    public LabourPaymentResponse toResponse(
            LabourPayment payment
    ) {

        LabourPaymentResponse response =
                new LabourPaymentResponse();

        Labour labour =
                payment.getLabour();

        response.setId(
                payment.getId()
        );

        response.setLabourId(
                labour.getId()
        );

        response.setLabourNo(
                labour.getLabourNo()
        );

        response.setLabourName(
                labour.getName()
        );

        response.setPaymentDate(
                payment.getPaymentDate()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setPaymentType(
                payment.getPaymentType()
        );

        response.setSalaryPeriodStart(
                payment.getSalaryPeriodStart()
        );

        response.setSalaryPeriodEnd(
                payment.getSalaryPeriodEnd()
        );

        response.setNotes(
                payment.getNotes()
        );

        response.setCreatedAt(
                payment.getCreatedAt()
        );

        response.setUpdatedAt(
                payment.getUpdatedAt()
        );

        return response;
    }
}