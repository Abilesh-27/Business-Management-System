package com.jp.business.service;

import com.jp.business.dto.PaymentRequest;
import com.jp.business.dto.PaymentResponse;
import com.jp.business.entity.Customer;
import com.jp.business.entity.Enquiry;
import com.jp.business.entity.Invoice;
import com.jp.business.entity.Payment;
import com.jp.business.entity.Quotation;
import com.jp.business.entity.WorkOrder;
import com.jp.business.repository.InvoiceRepository;
import com.jp.business.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public Payment createPayment(
            PaymentRequest request
    ) {

        Invoice invoice = invoiceRepository
                .findById(request.getInvoiceId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invoice not found: "
                                        + request.getInvoiceId()
                        )
                );

        BigDecimal amount = request.getAmount();

        BigDecimal totalAmount =
                invoice.getTotalAmount() != null
                        ? invoice.getTotalAmount()
                        : BigDecimal.ZERO;

        BigDecimal paidAmount =
                invoice.getPaidAmount() != null
                        ? invoice.getPaidAmount()
                        : BigDecimal.ZERO;

        BigDecimal balanceAmount =
                totalAmount.subtract(paidAmount);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(
                    "Payment amount must be greater than zero"
            );
        }

        if (amount.compareTo(balanceAmount) > 0) {
            throw new RuntimeException(
                    "Payment amount cannot be greater than "
                            + "invoice balance. Balance: "
                            + balanceAmount
            );
        }

        Payment payment = new Payment();

        payment.setPaymentNo(
                generatePaymentNumber()
        );

        payment.setInvoice(invoice);

        payment.setAmount(amount);

        payment.setPaymentDate(
                request.getPaymentDate() != null
                        ? request.getPaymentDate()
                        : LocalDate.now()
        );

        payment.setPaymentMethod(
                request.getPaymentMethod().toUpperCase()
        );

        payment.setReferenceNo(
                request.getReferenceNo()
        );

        payment.setNotes(
                request.getNotes()
        );

        BigDecimal newPaidAmount =
                paidAmount.add(amount);

        BigDecimal newBalanceAmount =
                totalAmount.subtract(newPaidAmount);

        if (newBalanceAmount.compareTo(
                BigDecimal.ZERO
        ) < 0) {
            newBalanceAmount = BigDecimal.ZERO;
        }

        invoice.setPaidAmount(
                newPaidAmount
        );

        invoice.setBalanceAmount(
                newBalanceAmount
        );

        if (newBalanceAmount.compareTo(
                BigDecimal.ZERO
        ) == 0) {

            invoice.setStatus("PAID");

        } else {

            invoice.setStatus("PARTIALLY_PAID");
        }

        invoiceRepository.save(invoice);

        return paymentRepository.save(payment);
    }

    private String generatePaymentNumber() {

        long count =
                paymentRepository.count() + 1;

        return String.format(
                "PAY-%04d",
                count
        );
    }

    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Payment getPaymentById(
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
    public List<Payment> getPaymentsByInvoice(
            Long invoiceId
    ) {

        return paymentRepository
                .findByInvoiceId(invoiceId);
    }

    @Transactional(readOnly = true)
    public PaymentResponse toResponse(
            Payment payment
    ) {

        PaymentResponse response =
                new PaymentResponse();

        response.setId(
                payment.getId()
        );

        response.setPaymentNo(
                payment.getPaymentNo()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setPaymentDate(
                payment.getPaymentDate()
        );

        response.setPaymentMethod(
                payment.getPaymentMethod()
        );

        response.setReferenceNo(
                payment.getReferenceNo()
        );

        response.setNotes(
                payment.getNotes()
        );

        Invoice invoice =
                payment.getInvoice();

        response.setInvoiceId(
                invoice.getId()
        );

        response.setInvoiceNo(
                invoice.getInvoiceNo()
        );

        response.setInvoiceTotalAmount(
                invoice.getTotalAmount()
        );

        response.setInvoicePaidAmount(
                invoice.getPaidAmount()
        );

        response.setInvoiceBalanceAmount(
                invoice.getBalanceAmount()
        );

        response.setInvoiceStatus(
                invoice.getStatus()
        );

        WorkOrder workOrder =
                invoice.getWorkOrder();

        if (workOrder != null) {

            response.setWorkOrderId(
                    workOrder.getId()
            );

            response.setWorkOrderNo(
                    workOrder.getWorkOrderNo()
            );

            Quotation quotation =
                    workOrder.getQuotation();

            if (quotation != null) {

                response.setQuotationId(
                        quotation.getId()
                );

                response.setQuotationNo(
                        quotation.getQuotationNo()
                );

                Enquiry enquiry =
                        quotation.getEnquiry();

                if (enquiry != null) {

                    response.setEnquiryId(
                            enquiry.getId()
                    );

                    response.setEnquiryNo(
                            enquiry.getEnquiryNo()
                    );

                    Customer customer =
                            enquiry.getCustomer();

                    if (customer != null) {

                        response.setCustomerId(
                                customer.getId()
                        );

                        response.setCustomerName(
                                customer.getName()
                        );
                    }
                }
            }
        }

        response.setCreatedAt(
                payment.getCreatedAt()
        );

        return response;
    }
}