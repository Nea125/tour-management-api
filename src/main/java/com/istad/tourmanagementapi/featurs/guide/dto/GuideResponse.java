package com.istad.tourmanagementapi.featurs.guide.dto;

import com.istad.tourmanagementapi.featurs.enums.GuideStatus;

public record GuideResponse(
        Long id,
        Long userId,
        String licenseNumber,
        Integer experienceYears,
        String languages,
        String bio,
        GuideStatus status
) {
}
