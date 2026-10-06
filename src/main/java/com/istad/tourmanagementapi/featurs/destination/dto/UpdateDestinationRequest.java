package com.istad.tourmanagementapi.featurs.destination.dto;

public record UpdateDestinationRequest(

        String name,
        String description,
        String province,
        String country,
        Double latitude,
        Double longitude
) {
}