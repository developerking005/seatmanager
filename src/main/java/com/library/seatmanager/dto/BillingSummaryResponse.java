package com.library.seatmanager.dto;

public record BillingSummaryResponse(
        int totalRevenue,
        int totalExpenses,
        int netProfit,
        int projectedIncome
) {}
