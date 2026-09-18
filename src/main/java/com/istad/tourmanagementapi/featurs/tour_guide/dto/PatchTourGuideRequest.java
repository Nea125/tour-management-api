package com.istad.tourmanagementapi.featurs.tour_guide.dto;

import java.util.List;

public record PatchTourGuideRequest(

        String userId,

        String licenseNumber,

        Integer experienceYears,

        List<String> languages,

        String bio

) {
}