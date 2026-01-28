package com.library.seatmanager.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category; // Rent, Wifi, Electricity
    private int amount;

    private LocalDate expenseDate;

    @Enumerated(EnumType.STRING)
    private ExpenseStatus status; // PAID / PENDING

    private String receipt;

    public Expense(Long id, String receipt, LocalDate expenseDate, ExpenseStatus status, int amount, String category) {
        this.id = id;
        this.receipt = receipt;
        this.expenseDate = expenseDate;
        this.status = status;
        this.amount = amount;
        this.category = category;
    }


    public Expense() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public ExpenseStatus getStatus() {
        return status;
    }

    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", expenseDate=" + expenseDate +
                ", status=" + status +
                ", receipt='" + receipt + '\'' +
                '}';
    }



    public enum ExpenseStatus {
        PAID,
        PENDING
    }
}

