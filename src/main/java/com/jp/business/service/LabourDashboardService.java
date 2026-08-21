package com.jp.business.service;

import com.jp.business.dto.LabourDashboardResponse;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LabourDashboardService {

    private static final BigDecimal ZERO =
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final LabourRepository labourRepository;
    private final LabourWorkEntryRepository workEntryRepository;
    private final LabourPaymentRepository paymentRepository;

    public LabourDashboardService(
            LabourRepository labourRepository,
            LabourWorkEntryRepository workEntryRepository,
            LabourPaymentRepository paymentRepository
    ) {
        this.labourRepository = labourRepository;
        this.workEntryRepository = workEntryRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public LabourDashboardResponse getTodayDashboard() {

        LocalDate today = LocalDate.now();

        List<Labour> labours =
                labourRepository.findAll();

        List<LabourWorkEntry> todayEntries =
                workEntryRepository
                        .findByWorkDateOrderByWorkDateAsc(today);

        long totalLabours =
                labours.stream()
                        .filter(Labour::isActive)
                        .count();

        Set<Long> workingLabourIds =
                new HashSet<>();

        Set<Long> overtimeLabourIds =
                new HashSet<>();

        BigDecimal totalWorkingHours = ZERO;
        BigDecimal totalOvertimeHours = ZERO;
        BigDecimal todayLabourCost = ZERO;

        for (LabourWorkEntry entry : todayEntries) {

            if (entry.getLabour() == null) {
                continue;
            }

            Long labourId =
                    entry.getLabour().getId();

            workingLabourIds.add(labourId);

            if (entry.getOvertimeHours() != null
                    && entry.getOvertimeHours()
                    .compareTo(BigDecimal.ZERO) > 0) {

                overtimeLabourIds.add(labourId);
            }

            if (entry.getTotalHours() != null) {
                totalWorkingHours =
                        totalWorkingHours.add(
                                entry.getTotalHours()
                        );
            }

            if (entry.getOvertimeHours() != null) {
                totalOvertimeHours =
                        totalOvertimeHours.add(
                                entry.getOvertimeHours()
                        );
            }

            if (entry.getEarningAmount() != null) {
                todayLabourCost =
                        todayLabourCost.add(
                                entry.getEarningAmount()
                        );
            }
        }

        /*
         * Calculate outstanding advances from
         * all payment history.
         */
        BigDecimal totalAdvanceGiven = ZERO;
        BigDecimal totalAdvanceRecovered = ZERO;

        List<LabourPayment> allPayments =
                paymentRepository.findAll();

        for (LabourPayment payment : allPayments) {

            if (payment.getAmount() == null) {
                continue;
            }

            if (payment.getPaymentType()
                    == LabourPaymentType.ADVANCE) {

                totalAdvanceGiven =
                        totalAdvanceGiven.add(
                                payment.getAmount()
                        );
            }

            if (payment.getPaymentType()
                    == LabourPaymentType.ADVANCE_RECOVERY) {

                totalAdvanceRecovered =
                        totalAdvanceRecovered.add(
                                payment.getAmount()
                        );
            }
        }

        BigDecimal outstandingAdvance =
                totalAdvanceGiven.subtract(
                        totalAdvanceRecovered
                );

        if (outstandingAdvance.compareTo(
                BigDecimal.ZERO
        ) < 0) {
            outstandingAdvance = ZERO;
        }

        /*
         * Pending salary is calculated from
         * all work earnings and all salary payments.
         */
        BigDecimal totalWorkEarnings = ZERO;
        BigDecimal totalSalaryPaid = ZERO;

        List<LabourWorkEntry> allWorkEntries =
                workEntryRepository.findAll();

        for (LabourWorkEntry entry : allWorkEntries) {

            if (entry.getEarningAmount() != null) {
                totalWorkEarnings =
                        totalWorkEarnings.add(
                                entry.getEarningAmount()
                        );
            }
        }

        for (LabourPayment payment : allPayments) {

            if (payment.getAmount() == null) {
                continue;
            }

            if (payment.getPaymentType()
                    == LabourPaymentType.SALARY) {

                totalSalaryPaid =
                        totalSalaryPaid.add(
                                payment.getAmount()
                        );
            }
        }

        BigDecimal pendingSalary =
                totalWorkEarnings.subtract(
                        totalSalaryPaid
                );

        if (pendingSalary.compareTo(
                BigDecimal.ZERO
        ) < 0) {
            pendingSalary = ZERO;
        }

        LabourDashboardResponse response =
                new LabourDashboardResponse();

        response.setTotalLabours(
                totalLabours
        );

        response.setWorkingToday(
                workingLabourIds.size()
        );

        response.setOvertimeWorkers(
                overtimeLabourIds.size()
        );

        response.setTotalWorkingHours(
                totalWorkingHours.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        response.setTotalOvertimeHours(
                totalOvertimeHours.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        response.setTodayLabourCost(
                todayLabourCost.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        response.setPendingSalary(
                pendingSalary.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        response.setOutstandingAdvance(
                outstandingAdvance.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        return response;
    }
}