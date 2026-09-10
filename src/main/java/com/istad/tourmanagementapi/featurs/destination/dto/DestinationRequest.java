package com.istad.tourmanagementapi.featurs.destination.dto;

import com.istad.tourmanagementapi.featurs.enums.DestinationStatus;

import java.util.List;

public record DestinationRequest(
        String name,
        String description,
        String province,
        String country,
        Long latitude,
        Long longitude,
        List<String> imageUrl,
        DestinationStatus status
) {
}
