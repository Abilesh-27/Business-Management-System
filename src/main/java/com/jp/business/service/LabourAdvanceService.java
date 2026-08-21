package com.jp.business.service;

import com.jp.business.dto.LabourAdvanceSummaryResponse;
import com.jp.business.entity.Labour;
import com.jp.business.entity.LabourPayment;
import com.jp.business.entity.LabourPaymentType;
import com.jp.business.repository.LabourPaymentRepository;
import com.jp.business.repository.LabourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LabourAdvanceService {

    private static final BigDecimal ZERO =
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final LabourRepository labourRepository;
    private final LabourPaymentRepository paymentRepository;

    public LabourAdvanceService(
            LabourRepository labourRepository,
            LabourPaymentRepository paymentRepository
    ) {
        this.labourRepository = labourRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public LabourAdvanceSummaryResponse
    getAdvanceSummary(Long labourId) {

        Labour labour =
                labourRepository.findById(labourId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Labour not found: " + labourId
                                )
                        );

        List<LabourPayment> payments =
                paymentRepository
                        .findByLabourIdOrderByPaymentDateDesc(
                                labourId
                        );

        BigDecimal totalAdvanceGiven = ZERO;
        BigDecimal totalAdvanceRecovered = ZERO;

        for (LabourPayment payment : payments) {

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

        LabourAdvanceSummaryResponse response =
                new LabourAdvanceSummaryResponse();

        response.setLabourId(
                labour.getId()
        );

        response.setLabourNo(
                labour.getLabourNo()
        );

        response.setLabourName(
                labour.getName()
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

        return response;
    }
}