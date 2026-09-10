package com.istad.tourmanagementapi.featurs.report.dto;

import java.math.BigDecimal;

public record DashboardResponse(
        Long completedTours,
        Long totalBookings,
        Long confirmedBookings,
        Long cancelledBookings,
        Long pendingBookings,
        Long totalParticipants,
        BigDecimal totalRevenue
) {
}