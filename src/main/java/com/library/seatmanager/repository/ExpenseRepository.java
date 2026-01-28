package com.library.seatmanager.repository;

import com.library.seatmanager.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.status = 'PAID'")
    int sumPaidExpenses();


    // latest expenses first
    List<Expense> findTop10ByOrderByExpenseDateDesc();
}
