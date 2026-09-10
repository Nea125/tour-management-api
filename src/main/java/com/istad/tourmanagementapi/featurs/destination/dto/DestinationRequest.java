package com.istad.tourmanagementapi.featurs.destination.dto;

import com.istad.tourmanagementapi.featurs.enums.DestinationStatus;

import java.util.List;
import jakarta.validation.constraints.*;


public record DestinationRequest(

        @NotBlank(message = "Name is required") @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Province is required")

        String province,

        @NotBlank(message = "Country is required")  @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        @NotNull(message = "Latitude is required")
        Long latitude,

        @NotNull(message = "Longitude is required")
        Long longitude,

        @NotEmpty(message = "At least one image is required")
        List<@NotBlank(message = "Image URL cannot be blank") String> imageUrl
) {
}
