package com.istad.tourmanagementapi.featurs.tour.dto;

import com.istad.tourmanagementapi.featurs.enums.TourStatus;

import java.math.BigDecimal;
import java.util.List;

public record TourResponse(
        Long id,
        Long destinationId,
        String title,
        String description,
        Integer durationDays,
        Integer durationNights,
        Integer maxParticipants,
        BigDecimal price,
        TourStatus status,
        List<String> images
) {
}
