package com.istad.tourmanagementapi.featurs.tour.dto;

import com.istad.tourmanagementapi.featurs.enums.TourStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record TourRequest(

        @NotNull(message = "Destination ID is required")
        @Positive(message = "Destination ID must be positive")
        Long destinationId,

        @NotBlank(message = "Title is required")

        String title,

        @NotBlank(message = "Description is required")

        String description,

        @NotNull(message = "Duration days is required")
        Integer durationDays,

        @NotNull(message = "Duration nights is required")
        Integer durationNights,

        @NotNull(message = "Maximum participants is required")
        Integer maxParticipants,

        @NotNull(message = "Price is required")
        BigDecimal price,

        @NotEmpty(message = "At least one image is required")
        List<@NotBlank(message = "Image URL cannot be blank") String> images

) {
}