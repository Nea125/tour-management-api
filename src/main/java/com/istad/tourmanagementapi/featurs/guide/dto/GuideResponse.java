package com.istad.tourmanagementapi.featurs.guide.dto;

import com.istad.tourmanagementapi.featurs.enums.GuideStatus;

import java.util.List;

public record GuideResponse(
        Long id,
        Long userId,
        String licenseNumber,
        Integer experienceYears,
        List<String> languages,
        String bio,
        GuideStatus status
) {
}
