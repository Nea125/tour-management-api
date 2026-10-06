package com.istad.tourmanagementapi.featurs.destination.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/** Image ids in the new display order. The first id becomes the cover. */
public record ReorderImagesRequest(
        @NotEmpty(message = "imageIds is required")
        List<Integer> imageIds
) {
}