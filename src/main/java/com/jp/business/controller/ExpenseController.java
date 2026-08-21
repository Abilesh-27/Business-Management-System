package com.jp.business.controller;

import com.jp.business.dto.ExpenseRequest;
import com.jp.business.dto.ExpenseResponse;
import com.jp.business.entity.Expense;
import com.jp.business.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:5173")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(
            ExpenseService expenseService
    ) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request
    ) {

        Expense expense =
                expenseService.createExpense(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        expenseService.toResponse(expense)
                );
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>>
    getAllExpenses() {

        List<ExpenseResponse> response =
                expenseService
                        .getAllExpenses()
                        .stream()
                        .map(expenseService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse>
    getExpense(
            @PathVariable Long id
    ) {

        Expense expense =
                expenseService.getExpenseById(id);

        return ResponseEntity.ok(
                expenseService.toResponse(expense)
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ExpenseResponse>>
    getExpensesByCategory(
            @PathVariable String category
    ) {

        List<ExpenseResponse> response =
                expenseService
                        .getExpensesByCategory(category)
                        .stream()
                        .map(expenseService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ExpenseResponse>>
    getExpensesByDate(
            @PathVariable LocalDate date
    ) {

        List<ExpenseResponse> response =
                expenseService
                        .getExpensesByDate(date)
                        .stream()
                        .map(expenseService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/between")
    public ResponseEntity<List<ExpenseResponse>>
    getExpensesBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        List<ExpenseResponse> response =
                expenseService
                        .getExpensesBetweenDates(
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(expenseService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payment-method/{paymentMethod}")
    public ResponseEntity<List<ExpenseResponse>>
    getExpensesByPaymentMethod(
            @PathVariable String paymentMethod
    ) {

        List<ExpenseResponse> response =
                expenseService
                        .getExpensesByPaymentMethod(
                                paymentMethod
                        )
                        .stream()
                        .map(expenseService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}