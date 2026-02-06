package com.library.seatmanager.controller;

import com.library.seatmanager.dto.BillingSummaryResponse;
import com.library.seatmanager.entity.Expense;
import com.library.seatmanager.entity.Library;
import com.library.seatmanager.repository.ExpenseRepository;
import com.library.seatmanager.repository.LibraryRepository;
import com.library.seatmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BillingController {


    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private LibraryRepository libraryRepo;


    @GetMapping("/billing/summary/{libraryId}")
    public BillingSummaryResponse getMonthlySummary(
            @PathVariable Long libraryId,
            @RequestParam int year,
            @RequestParam int month
    ) {

        int revenue =
                studentRepo.sumMonthlyRevenue(libraryId, year, month);

        System.out.println("Sum month of revenue : " + revenue);

        int expenses =
                expenseRepo.sumMonthlyExpenses(libraryId, year, month);

        System.out.println("Sum month of expense : " + expenses);

        int profit = revenue - expenses;

        System.out.println("Total Profit : " + profit);

        double avgProfit =
                calculateAverageMonthlyProfit(libraryId);

        return new BillingSummaryResponse(
                revenue,
                expenses,
                profit,
                avgProfit
        );
    }

    private double calculateAverageMonthlyProfit(Long libraryId) {
        // simple version: last 6 months
        double total = 0;
        int count = 0;

        LocalDate now = LocalDate.now();

        for (int i = 0; i < 6; i++) {
            LocalDate d = now.minusMonths(i);
            int r = studentRepo.sumMonthlyRevenue(
                    libraryId, d.getYear(), d.getMonthValue()
            );
            int e = expenseRepo.sumMonthlyExpenses(
                    libraryId, d.getYear(), d.getMonthValue()
            );
            total += (r - e);
            count++;
        }
        return count == 0 ? 0 : total / count;
    }


    @GetMapping("/billing/expenses/library/{libraryId}")
    public List<Expense> getAllExpenses(
            @PathVariable Long libraryId
    ) {
        System.out.println("Fetching ALL expenses for library: " + libraryId);
        return expenseRepo.findByLibrary_IdOrderByExpenseDateDesc(libraryId);
    }

    @PostMapping("/expenses/library/{libraryId}")
    public Expense addExpense(
            @PathVariable Long libraryId,
            @RequestBody Expense expense
    ) {
        Library library = libraryRepo.findById(libraryId)
                .orElseThrow(() -> new RuntimeException("Library not found"));

        expense.setLibrary(library);   // 🔥 THIS WAS MISSING
        return expenseRepo.save(expense);
    }
    // 🔹 Update expense
    @PutMapping("/billing/expenses/{id}/library/{libraryId}")
    public Expense updateExpense(
            @PathVariable Long id,
            @PathVariable Long libraryId,
            @RequestBody Expense req
    ) {
        Expense e = expenseRepo.findById(id).orElseThrow();

        if (!e.getLibrary().getId().equals(libraryId)) {
            throw new RuntimeException("Unauthorized");
        }

        e.setCategory(req.getCategory());
        e.setAmount(req.getAmount());
        e.setExpenseDate(req.getExpenseDate());
        e.setStatus(req.getStatus());

        return expenseRepo.save(e);
    }

    // 🔹 Delete expense
    @DeleteMapping("/billing/expenses/{id}/library/{libraryId}")
    public void deleteExpense(@PathVariable Long id) {
        expenseRepo.deleteById(id);
    }
}
