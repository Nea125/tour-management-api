package com.istad.tourmanagementapi.featurs.report.dto;

import java.math.BigDecimal;

public record MonthlyRevenueResponse(
        String label,
        BigDecimal revenue
) {
}