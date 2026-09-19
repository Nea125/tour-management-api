package com.istad.tourmanagementapi.featurs.tour.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTourRequest(

        @NotNull(message = "Destination ID is required")
        @Positive(message = "Destination ID must be positive")
        Long destinationId,

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Duration days is required")
        @Positive(message = "Duration days must be positive")
        Integer durationDays,

        @NotNull(message = "Duration nights is required")
        @Positive(message = "Duration nights must be positive")
        Integer durationNights,

        @NotNull(message = "Maximum participants is required")
        @Positive(message = "Maximum participants must be positive")
        Integer maxParticipants,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price

) {
}