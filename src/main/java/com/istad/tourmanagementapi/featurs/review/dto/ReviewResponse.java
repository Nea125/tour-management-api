package com.istad.tourmanagementapi.featurs.review.dto;
import java.time.LocalDateTime;
public record ReviewResponse(
        Long id,
        Long bookingId,
        Long tourId,
        String userId,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}