package com.istad.tourmanagementapi.featurs.tour.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateTourRequest(

        @Positive(message = "Destination ID must be positive")
        Long destinationId,

        String title,

        String description,

        @Positive(message = "Duration days must be positive")
        Integer durationDays,

        @Positive(message = "Duration nights must be positive")
        Integer durationNights,

        @Positive(message = "Maximum participants must be positive")
        Integer maxParticipants,

        @Positive(message = "Price must be positive")
        BigDecimal price

) {
}