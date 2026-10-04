package com.istad.tourmanagementapi.featurs.report.dto;

import java.math.BigDecimal;

public record TopTourResponse(
        Long id,
        String title,
        Long bookings,
        Double rating,
        BigDecimal revenue
) {
}