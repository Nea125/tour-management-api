package com.istad.tourmanagementapi.featurs.activity.dto;

import java.util.List;

public record ActivityResponse(
        Long id,
        Long tourId,
        String title,
        String description,
        List<ActivityTimelineResponse> timeline,
        boolean isDeleted
) {
}