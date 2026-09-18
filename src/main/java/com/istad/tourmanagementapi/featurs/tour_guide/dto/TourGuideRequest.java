package com.istad.tourmanagementapi.featurs.tour_guide.dto;

import com.istad.tourmanagementapi.featurs.enums.TourGuideStatus;

import java.util.List;

public record TourGuideRequest(
        String userId,
        String licenseNumber,
        Integer experienceYears,
        List<String> languages,
        String bio,
        TourGuideStatus status
) {
}
