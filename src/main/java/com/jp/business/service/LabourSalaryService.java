package com.jp.business.service;

import com.jp.business.dto.LabourSalarySummaryResponse;
import com.jp.business.entity.Labour;
import com.jp.business.entity.LabourPayment;
import com.jp.business.entity.LabourPaymentType;
import com.jp.business.entity.LabourWorkEntry;
import com.jp.business.repository.LabourPaymentRepository;
import com.jp.business.repository.LabourRepository;
import com.jp.business.repository.LabourWorkEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class LabourSalaryService {

    private static final BigDecimal ZERO =
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final LabourRepository labourRepository;
    private final LabourWorkEntryRepository workEntryRepository;
    private final LabourPaymentRepository paymentRepository;

    public LabourSalaryService(
            LabourRepository labourRepository,
            LabourWorkEntryRepository workEntryRepository,
            LabourPaymentRepository paymentRepository
    ) {
        this.labourRepository = labourRepository;
        this.workEntryRepository = workEntryRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public LabourSalarySummaryResponse
    getSalarySummary(
            Long labourId,
            LocalDate startDate,
            LocalDate endDate
    ) {

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

        Labour labour =
                labourRepository.findById(labourId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Labour not found: "
                                                + labourId
                                )
                        );

        List<LabourWorkEntry> workEntries =
                workEntryRepository
                        .findByLabourIdAndWorkDateBetweenOrderByWorkDateAsc(
                                labourId,
                                startDate,
                                endDate
                        );

        List<LabourPayment> payments =
                paymentRepository
                        .findByLabourIdAndPaymentDateBetweenOrderByPaymentDateAsc(
                                labourId,
                                startDate,
                                endDate
                        );

        BigDecimal totalHours = ZERO;
        BigDecimal normalHours = ZERO;
        BigDecimal overtimeHours = ZERO;
        BigDecimal totalWorkEarnings = ZERO;

        for (LabourWorkEntry entry : workEntries) {

            totalHours =
                    add(
                            totalHours,
                            entry.getTotalHours()
                    );

            normalHours =
                    add(
                            normalHours,
                            entry.getNormalHours()
                    );

            overtimeHours =
                    add(
                            overtimeHours,
                            entry.getOvertimeHours()
                    );

            totalWorkEarnings =
                    add(
                            totalWorkEarnings,
                            entry.getEarningAmount()
                    );
        }

        BigDecimal totalSalaryPaid = ZERO;
        BigDecimal totalAdvanceGiven = ZERO;
        BigDecimal totalAdvanceRecovered = ZERO;

        for (LabourPayment payment : payments) {

            BigDecimal amount =
                    payment.getAmount();

            if (amount == null) {
                continue;
            }

            switch (payment.getPaymentType()) {

                case SALARY:

                    totalSalaryPaid =
                            add(
                                    totalSalaryPaid,
                                    amount
                            );

                    break;

                case ADVANCE:

                    totalAdvanceGiven =
                            add(
                                    totalAdvanceGiven,
                                    amount
                            );

                    break;

                case ADVANCE_RECOVERY:

                    totalAdvanceRecovered =
                            add(
                                    totalAdvanceRecovered,
                                    amount
                            );

                    break;
            }
        }

        /*
         * Outstanding advance:
         *
         * Total advances given
         * -
         * Total advance recovered
         */
        BigDecimal outstandingAdvance =
                totalAdvanceGiven
                        .subtract(
                                totalAdvanceRecovered
                        );

        if (outstandingAdvance.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            outstandingAdvance = ZERO;
        }

        /*
         * Pending salary:
         *
         * Work earnings
         * -
         * Salary already paid
         *
         * Advances are tracked separately because
         * they are not salary payments.
         */
        BigDecimal pendingSalary =
                totalWorkEarnings
                        .subtract(
                                totalSalaryPaid
                        );

        if (pendingSalary.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            pendingSalary = ZERO;
        }

        /*
         * Total money actually given to labour:
         *
         * Salary payments + advances
         */
        BigDecimal totalAmountPaid =
                totalSalaryPaid
                        .add(
                                totalAdvanceGiven
                        );

        LabourSalarySummaryResponse response =
                new LabourSalarySummaryResponse();

        response.setLabourId(
                labour.getId()
        );

        response.setLabourNo(
                labour.getLabourNo()
        );

        response.setLabourName(
                labour.getName()
        );

        response.setStartDate(
                startDate
        );

        response.setEndDate(
                endDate
        );

        response.setDailyWage(
                labour.getDailyWage()
        );

        BigDecimal hourlyWage =
                labour.getDailyWage()
                        .divide(
                                BigDecimal.valueOf(8),
                                2,
                                RoundingMode.HALF_UP
                        );

        response.setHourlyWage(
                hourlyWage
        );

        response.setTotalHours(
                totalHours
        );

        response.setNormalHours(
                normalHours
        );

        response.setOvertimeHours(
                overtimeHours
        );

        response.setTotalWorkEarnings(
                totalWorkEarnings
        );

        response.setTotalSalaryPaid(
                totalSalaryPaid
        );

        response.setTotalAdvanceGiven(
                totalAdvanceGiven
        );

        response.setTotalAdvanceRecovered(
                totalAdvanceRecovered
        );

        response.setOutstandingAdvance(
                outstandingAdvance
        );

        response.setPendingSalary(
                pendingSalary
        );

        response.setTotalAmountPaid(
                totalAmountPaid
        );

        return response;
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
}