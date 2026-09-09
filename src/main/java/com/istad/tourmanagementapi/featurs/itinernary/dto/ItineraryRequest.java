package com.istad.tourmanagementapi.featurs.itinernary.dto;

public record ItineraryRequest(
        Long tourId,
        Integer dayNumber,
        String title,
        String description
) {
}
