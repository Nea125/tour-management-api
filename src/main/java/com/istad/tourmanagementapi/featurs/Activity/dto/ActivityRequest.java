package com.istad.tourmanagementapi.featurs.Activity.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ActivityRequest(

        @NotNull(message = "Tour ID is required")
        Long tourId,

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotEmpty(message = "Timeline is required")
        List<@Valid ActivityTimelineRequest> timeline

) {
}