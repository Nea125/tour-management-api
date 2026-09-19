package com.istad.tourmanagementapi.featurs.destination.dto;

import jakarta.validation.constraints.Size;

public record UpdateDestinationRequest(

        String name,
        String description,
        String province,
        String country,
        Double latitude,
        Double longitude
) {
}