package com.istad.tourmanagementapi.featurs.destination.dto;

import com.istad.tourmanagementapi.featurs.enums.DestinationStatus;

import java.util.List;

public record DestinationResponse(
        Long id,
        String name,
        String description,
        String province,
        String country,
        Double latitude,
        Double longitude,
        List<String> imageUrl,
        DestinationStatus status
) {
}
