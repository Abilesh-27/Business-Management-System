package com.jp.business.repository;

import com.jp.business.entity.LabourSalarySettlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LabourSalarySettlementRepository
        extends JpaRepository<LabourSalarySettlement, Long> {

    Optional<LabourSalarySettlement>
    findByLabourIdAndPeriodStartAndPeriodEnd(
            Long labourId,
            LocalDate periodStart,
            LocalDate periodEnd
    );

    List<LabourSalarySettlement>
    findByLabourIdOrderByPeriodStartDesc(
            Long labourId
    );

    List<LabourSalarySettlement>
    findByLabourIdAndPeriodStartGreaterThanEqualAndPeriodEndLessThanEqualOrderByPeriodStartAsc(
            Long labourId,
            LocalDate startDate,
            LocalDate endDate
    );
}