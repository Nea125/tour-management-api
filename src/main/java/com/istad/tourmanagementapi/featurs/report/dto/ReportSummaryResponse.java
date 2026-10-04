package com.istad.tourmanagementapi.featurs.report.dto;

import java.math.BigDecimal;

public record ReportSummaryResponse(
        BigDecimal totalRevenue,
        Long totalBookings,
        Long totalTours,
        Long totalDestinations,
        Double averageRating,
        Long totalCustomers,
        Long totalGuides,
        Long upcomingTours,
        Long completedTours,
        Long cancelledBookings
) {
}