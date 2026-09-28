package com.istad.tourmanagementapi.featurs.report.dto;

import java.math.BigDecimal;

public record ReportResponse(
        BigDecimal totalRevenue,
        Long totalBooking,
        Long totalTour,
        Long totalDestination,
        Double averageRating,
        Long totalCustomer,
        Long totalGuide,
        Long upcomingTour,
        Long completedTour,
        Long cancelledBooking
) {
}