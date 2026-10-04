package com.istad.tourmanagementapi.featurs.report.dto;

public record TopDestinationResponse(
        Long id,
        String name,
        Long bookings
) {
}