package com.jp.business.repository;

import com.jp.business.entity.LabourPayment;
import com.jp.business.entity.LabourPaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LabourPaymentRepository
        extends JpaRepository<LabourPayment, Long> {

    List<LabourPayment>
    findByLabourIdOrderByPaymentDateDesc(
            Long labourId
    );

    List<LabourPayment>
    findByLabourIdAndPaymentDateBetweenOrderByPaymentDateAsc(
            Long labourId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<LabourPayment>
    findByLabourIdAndPaymentTypeOrderByPaymentDateAsc(
            Long labourId,
            LabourPaymentType paymentType
    );

    List<LabourPayment>
    findByLabourIdAndPaymentTypeAndPaymentDateBetweenOrderByPaymentDateAsc(
            Long labourId,
            LabourPaymentType paymentType,
            LocalDate startDate,
            LocalDate endDate
    );
}