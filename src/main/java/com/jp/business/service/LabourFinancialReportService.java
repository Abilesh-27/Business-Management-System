package com.jp.business.service;

import com.jp.business.dto.LabourFinancialReportResponse;
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
public class LabourFinancialReportService {

    private static final BigDecimal ZERO =
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final LabourRepository labourRepository;
    private final LabourWorkEntryRepository workEntryRepository;
    private final LabourPaymentRepository paymentRepository;

    public LabourFinancialReportService(
            LabourRepository labourRepository,
            LabourWorkEntryRepository workEntryRepository,
            LabourPaymentRepository paymentRepository
    ) {
        this.labourRepository = labourRepository;
        this.workEntryRepository = workEntryRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public LabourFinancialReportResponse getFinancialReport(
            Long labourId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        validateDates(startDate, endDate);

        Labour labour =
                labourRepository.findById(labourId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Labour not found: " + labourId
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

        BigDecimal totalWorkingHours = ZERO;
        BigDecimal totalNormalHours = ZERO;
        BigDecimal totalOvertimeHours = ZERO;
        BigDecimal totalWorkEarnings = ZERO;

        for (LabourWorkEntry entry : workEntries) {

            totalWorkingHours =
                    add(
                            totalWorkingHours,
                            entry.getTotalHours()
                    );

            totalNormalHours =
                    add(
                            totalNormalHours,
                            entry.getNormalHours()
                    );

            totalOvertimeHours =
                    add(
                            totalOvertimeHours,
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

            if (payment.getAmount() == null) {
                continue;
            }

            switch (payment.getPaymentType()) {

                case SALARY:

                    totalSalaryPaid =
                            add(
                                    totalSalaryPaid,
                                    payment.getAmount()
                            );

                    break;

                case ADVANCE:

                    totalAdvanceGiven =
                            add(
                                    totalAdvanceGiven,
                                    payment.getAmount()
                            );

                    break;

                case ADVANCE_RECOVERY:

                    totalAdvanceRecovered =
                            add(
                                    totalAdvanceRecovered,
                                    payment.getAmount()
                            );

                    break;
            }
        }

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
         * Pending salary for the selected period.
         *
         * Advance recovery reduces the salary due.
         */
        BigDecimal salaryDue =
                totalWorkEarnings
                        .subtract(
                                totalAdvanceRecovered
                        );

        if (salaryDue.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            salaryDue = ZERO;
        }

        BigDecimal pendingSalary =
                salaryDue
                        .subtract(
                                totalSalaryPaid
                        );

        if (pendingSalary.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            pendingSalary = ZERO;
        }

        /*
         * Actual money given to the labour during
         * the selected period.
         *
         * Advance is included because it is money
         * actually received by the labour.
         */
        BigDecimal totalMoneyGiven =
                totalSalaryPaid
                        .add(
                                totalAdvanceGiven
                        );

        BigDecimal dailyWage =
                labour.getDailyWage();

        BigDecimal hourlyWage =
                dailyWage.divide(
                        BigDecimal.valueOf(8),
                        2,
                        RoundingMode.HALF_UP
                );

        LabourFinancialReportResponse response =
                new LabourFinancialReportResponse();

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
                dailyWage
        );

        response.setHourlyWage(
                hourlyWage
        );

        response.setTotalWorkingHours(
                totalWorkingHours
        );

        response.setTotalNormalHours(
                totalNormalHours
        );

        response.setTotalOvertimeHours(
                totalOvertimeHours
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

        response.setTotalMoneyGiven(
                totalMoneyGiven
        );

        return response;
    }

    private void validateDates(
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