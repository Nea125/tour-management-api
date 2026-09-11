package com.istad.tourmanagementapi.featurs.Activity.dto;

import jakarta.validation.constraints.NotBlank;

public record ActivityTimelineRequest(

        @NotBlank(message = "Time is required")
        String time,

        @NotBlank(message = "Activity is required")
        String activity
) {
}