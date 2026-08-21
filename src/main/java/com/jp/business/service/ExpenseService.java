package com.jp.business.service;

import com.jp.business.dto.ExpenseRequest;
import com.jp.business.dto.ExpenseResponse;
import com.jp.business.entity.Expense;
import com.jp.business.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository
    ) {
        this.expenseRepository = expenseRepository;
    }

    @Transactional
    public Expense createExpense(
            ExpenseRequest request
    ) {

        Expense expense = new Expense();

        expense.setExpenseNo(
                generateExpenseNumber()
        );

        expense.setCategory(
                request.getCategory().toUpperCase()
        );

        expense.setDescription(
                request.getDescription()
        );

        expense.setAmount(
                request.getAmount()
        );

        expense.setExpenseDate(
                request.getExpenseDate() != null
                        ? request.getExpenseDate()
                        : LocalDate.now()
        );

        if (request.getPaymentMethod() != null) {
            expense.setPaymentMethod(
                    request.getPaymentMethod().toUpperCase()
            );
        }

        expense.setReferenceNo(
                request.getReferenceNo()
        );

        expense.setNotes(
                request.getNotes()
        );

        return expenseRepository.save(expense);
    }

    private String generateExpenseNumber() {

        long count =
                expenseRepository.count() + 1;

        return String.format(
                "EXP-%04d",
                count
        );
    }

    @Transactional(readOnly = true)
    public List<Expense> getAllExpenses() {

        return expenseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Expense getExpenseById(
            Long id
    ) {

        return expenseRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Expense not found: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpensesByCategory(
            String category
    ) {

        return expenseRepository
                .findByCategory(
                        category.toUpperCase()
                );
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpensesByDate(
            LocalDate date
    ) {

        return expenseRepository
                .findByExpenseDate(date);
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpensesBetweenDates(
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException(
                    "Start date cannot be after end date"
            );
        }

        return expenseRepository
                .findByExpenseDateBetween(
                        startDate,
                        endDate
                );
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpensesByPaymentMethod(
            String paymentMethod
    ) {

        return expenseRepository
                .findByPaymentMethod(
                        paymentMethod.toUpperCase()
                );
    }

    @Transactional(readOnly = true)
    public ExpenseResponse toResponse(
            Expense expense
    ) {

        ExpenseResponse response =
                new ExpenseResponse();

        response.setId(
                expense.getId()
        );

        response.setExpenseNo(
                expense.getExpenseNo()
        );

        response.setCategory(
                expense.getCategory()
        );

        response.setDescription(
                expense.getDescription()
        );

        response.setAmount(
                expense.getAmount()
        );

        response.setExpenseDate(
                expense.getExpenseDate()
        );

        response.setPaymentMethod(
                expense.getPaymentMethod()
        );

        response.setReferenceNo(
                expense.getReferenceNo()
        );

        response.setNotes(
                expense.getNotes()
        );

        response.setCreatedAt(
                expense.getCreatedAt()
        );

        response.setUpdatedAt(
                expense.getUpdatedAt()
        );

        return response;
    }
}