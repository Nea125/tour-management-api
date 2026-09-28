package com.istad.tourmanagementapi.featurs.review.dto;

public record ReviewResponse(
        Long id,
        Long bookingId,
        Long tourId,
        String userId,
        Integer rating,
        String comment
) {
}