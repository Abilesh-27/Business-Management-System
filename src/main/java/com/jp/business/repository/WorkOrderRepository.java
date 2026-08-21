package com.jp.business.repository;

import com.jp.business.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkOrderRepository
        extends JpaRepository<WorkOrder, Long> {

    Optional<WorkOrder> findByWorkOrderNo(String workOrderNo);

    List<WorkOrder> findByQuotationId(Long quotationId);

    List<WorkOrder> findByStatus(String status);
}