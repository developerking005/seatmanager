package com.library.seatmanager.controller;

import com.library.seatmanager.dto.BillingSummaryResponse;
import com.library.seatmanager.entity.Expense;
import com.library.seatmanager.repository.ExpenseRepository;
import com.library.seatmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BillingController {


    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    @GetMapping("/billing/summary")
    public BillingSummaryResponse getBillingSummary() {

        int totalRevenue = studentRepo.sumAmountPaidByActive();
        int totalExpenses = expenseRepo.sumPaidExpenses();

        int netProfit = totalRevenue - totalExpenses;

        int projectedIncome = studentRepo.sumMonthlyAmountByActive();

        return new BillingSummaryResponse(
                totalRevenue,
                totalExpenses,
                netProfit,
                projectedIncome
        );
    }

    @GetMapping("/billing/expenses")
    public List<Expense> getRecentExpenses() {
        return expenseRepo.findTop10ByOrderByExpenseDateDesc();
    }

    @PostMapping("/expenses")
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseRepo.save(expense);
    }

    // 🔹 Update expense
    @PutMapping("/billing/expenses/{id}")
    public Expense updateExpense(
            @PathVariable Long id,
            @RequestBody Expense req
    ) {
        Expense e = expenseRepo.findById(id)
                .orElseThrow();

        e.setCategory(req.getCategory());
        e.setAmount(req.getAmount());
        e.setExpenseDate(req.getExpenseDate());
        e.setStatus(req.getStatus());

        return expenseRepo.save(e);
    }

    // 🔹 Delete expense
    @DeleteMapping("/billing/expenses/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseRepo.deleteById(id);
    }
}
