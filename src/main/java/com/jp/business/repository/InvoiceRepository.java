package com.jp.business.repository;

import com.jp.business.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNo(String invoiceNo);

    List<Invoice> findByWorkOrderId(Long workOrderId);

    List<Invoice> findByStatus(String status);
}