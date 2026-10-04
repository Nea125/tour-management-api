package com.istad.tourmanagementapi.featurs.destination.dto;
import java.util.List;

public record DestinationResponse(
        Long id,
        String name,
        String description,
        String province,
        String country,
        Double latitude,
        Double longitude,
        List<Integer> imageIds,
        boolean isDeleted

) {
}
