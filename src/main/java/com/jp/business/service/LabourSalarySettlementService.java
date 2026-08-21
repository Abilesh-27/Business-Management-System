package com.jp.business.service;

import com.jp.business.dto.LabourSalarySettlementRequest;
import com.jp.business.dto.LabourSalarySettlementResponse;
import com.jp.business.entity.Labour;
import com.jp.business.entity.LabourPayment;
import com.jp.business.entity.LabourPaymentType;
import com.jp.business.entity.LabourSalarySettlement;
import com.jp.business.entity.SalarySettlementStatus;
import com.jp.business.entity.LabourWorkEntry;
import com.jp.business.repository.LabourPaymentRepository;
import com.jp.business.repository.LabourRepository;
import com.jp.business.repository.LabourSalarySettlementRepository;
import com.jp.business.repository.LabourWorkEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class LabourSalarySettlementService {

    private static final BigDecimal ZERO =
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final LabourRepository labourRepository;
    private final LabourWorkEntryRepository workEntryRepository;
    private final LabourPaymentRepository paymentRepository;
    private final LabourSalarySettlementRepository settlementRepository;

    public LabourSalarySettlementService(
            LabourRepository labourRepository,
            LabourWorkEntryRepository workEntryRepository,
            LabourPaymentRepository paymentRepository,
            LabourSalarySettlementRepository settlementRepository
    ) {
        this.labourRepository = labourRepository;
        this.workEntryRepository = workEntryRepository;
        this.paymentRepository = paymentRepository;
        this.settlementRepository = settlementRepository;
    }

    @Transactional
    public LabourSalarySettlement createSettlement(
            LabourSalarySettlementRequest request
    ) {

        validateDates(
                request.getPeriodStart(),
                request.getPeriodEnd()
        );

        Labour labour =
                labourRepository.findById(
                        request.getLabourId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Labour not found: "
                                        + request.getLabourId()
                        )
                );

        LabourSalarySettlement existing =
                settlementRepository
                        .findByLabourIdAndPeriodStartAndPeriodEnd(
                                request.getLabourId(),
                                request.getPeriodStart(),
                                request.getPeriodEnd()
                        )
                        .orElse(null);

        if (existing != null) {
            throw new RuntimeException(
                    "Salary settlement already exists for this labour and period"
            );
        }

        LabourSalarySettlement settlement =
                calculateSettlement(
                        labour,
                        request.getPeriodStart(),
                        request.getPeriodEnd()
                );

        settlement.setNotes(
                request.getNotes()
        );

        return settlementRepository.save(settlement);
    }

    @Transactional
    public LabourSalarySettlement
    recalculateSettlement(Long settlementId) {

        LabourSalarySettlement settlement =
                settlementRepository.findById(settlementId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary settlement not found: "
                                                + settlementId
                                )
                        );

        Labour labour =
                settlement.getLabour();

        LabourSalarySettlement recalculated =
                calculateSettlement(
                        labour,
                        settlement.getPeriodStart(),
                        settlement.getPeriodEnd()
                );

        settlement.setTotalWorkEarnings(
                recalculated.getTotalWorkEarnings()
        );

        settlement.setSalaryAlreadyPaid(
                recalculated.getSalaryAlreadyPaid()
        );

        settlement.setAdvanceRecovered(
                recalculated.getAdvanceRecovered()
        );

        settlement.setSalaryDue(
                recalculated.getSalaryDue()
        );

        settlement.setSalaryRemaining(
                recalculated.getSalaryRemaining()
        );

        settlement.setStatus(
                recalculated.getStatus()
        );

        return settlementRepository.save(settlement);
    }

    private LabourSalarySettlement calculateSettlement(
            Labour labour,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {

        List<LabourWorkEntry> workEntries =
                workEntryRepository
                        .findByLabourIdAndWorkDateBetweenOrderByWorkDateAsc(
                                labour.getId(),
                                periodStart,
                                periodEnd
                        );

        List<LabourPayment> payments =
                paymentRepository
                        .findByLabourIdAndPaymentDateBetweenOrderByPaymentDateAsc(
                                labour.getId(),
                                periodStart,
                                periodEnd
                        );

        BigDecimal totalWorkEarnings = ZERO;
        BigDecimal salaryAlreadyPaid = ZERO;
        BigDecimal advanceRecovered = ZERO;

        for (LabourWorkEntry entry : workEntries) {

            totalWorkEarnings =
                    add(
                            totalWorkEarnings,
                            entry.getEarningAmount()
                    );
        }

        for (LabourPayment payment : payments) {

            if (payment.getAmount() == null) {
                continue;
            }

            if (payment.getPaymentType()
                    == LabourPaymentType.SALARY) {

                salaryAlreadyPaid =
                        add(
                                salaryAlreadyPaid,
                                payment.getAmount()
                        );
            }

            if (payment.getPaymentType()
                    == LabourPaymentType.ADVANCE_RECOVERY) {

                advanceRecovered =
                        add(
                                advanceRecovered,
                                payment.getAmount()
                        );
            }
        }

        /*
         * Salary due before considering actual salary payments.
         *
         * Advance recovery reduces the amount remaining
         * from the salary.
         */
        BigDecimal salaryDue =
                totalWorkEarnings
                        .subtract(advanceRecovered);

        if (salaryDue.compareTo(BigDecimal.ZERO) < 0) {
            salaryDue = ZERO;
        }

        BigDecimal salaryRemaining =
                salaryDue
                        .subtract(salaryAlreadyPaid);

        if (salaryRemaining.compareTo(BigDecimal.ZERO) < 0) {
            salaryRemaining = ZERO;
        }

        SalarySettlementStatus status;

        if (salaryAlreadyPaid.compareTo(ZERO) == 0) {

            status = SalarySettlementStatus.PENDING;

        } else if (salaryRemaining.compareTo(ZERO) == 0) {

            status = SalarySettlementStatus.PAID;

        } else {

            status =
                    SalarySettlementStatus.PARTIALLY_PAID;
        }

        LabourSalarySettlement settlement =
                new LabourSalarySettlement();

        settlement.setLabour(labour);

        settlement.setPeriodStart(
                periodStart
        );

        settlement.setPeriodEnd(
                periodEnd
        );

        settlement.setTotalWorkEarnings(
                totalWorkEarnings
        );

        settlement.setSalaryAlreadyPaid(
                salaryAlreadyPaid
        );

        settlement.setAdvanceRecovered(
                advanceRecovered
        );

        settlement.setSalaryDue(
                salaryDue
        );

        settlement.setSalaryRemaining(
                salaryRemaining
        );

        settlement.setStatus(status);

        return settlement;
    }

    @Transactional(readOnly = true)
    public LabourSalarySettlement
    getSettlementById(Long id) {

        return settlementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Salary settlement not found: "
                                        + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<LabourSalarySettlement>
    getLabourSettlements(Long labourId) {

        if (!labourRepository.existsById(labourId)) {
            throw new RuntimeException(
                    "Labour not found: " + labourId
            );
        }

        return settlementRepository
                .findByLabourIdOrderByPeriodStartDesc(
                        labourId
                );
    }

    private void validateDates(
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (startDate == null || endDate == null) {
            throw new RuntimeException(
                    "Period start and end dates are required"
            );
        }

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException(
                    "Period start cannot be after period end"
            );
        }
    }

    private BigDecimal add(
            BigDecimal first,
            BigDecimal second
    ) {

        if (first == null) {
            first = ZERO;
        }

        if (second == null) {
            second = ZERO;
        }

        return first
                .add(second)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }

    public LabourSalarySettlementResponse
    toResponse(
            LabourSalarySettlement settlement
    ) {

        LabourSalarySettlementResponse response =
                new LabourSalarySettlementResponse();

        Labour labour =
                settlement.getLabour();

        response.setId(
                settlement.getId()
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

        response.setPeriodStart(
                settlement.getPeriodStart()
        );

        response.setPeriodEnd(
                settlement.getPeriodEnd()
        );

        response.setTotalWorkEarnings(
                settlement.getTotalWorkEarnings()
        );

        response.setSalaryAlreadyPaid(
                settlement.getSalaryAlreadyPaid()
        );

        response.setAdvanceRecovered(
                settlement.getAdvanceRecovered()
        );

        response.setSalaryDue(
                settlement.getSalaryDue()
        );

        response.setSalaryRemaining(
                settlement.getSalaryRemaining()
        );

        response.setStatus(
                settlement.getStatus()
        );

        response.setNotes(
                settlement.getNotes()
        );

        response.setCreatedAt(
                settlement.getCreatedAt()
        );

        response.setUpdatedAt(
                settlement.getUpdatedAt()
        );

        return response;
    }
}