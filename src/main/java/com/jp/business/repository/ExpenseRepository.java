package com.jp.business.repository;

import com.jp.business.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    Optional<Expense> findByExpenseNo(String expenseNo);

    List<Expense> findByCategory(String category);

    List<Expense> findByExpenseDate(LocalDate expenseDate);

    List<Expense> findByExpenseDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    List<Expense> findByPaymentMethod(
            String paymentMethod
    );
}