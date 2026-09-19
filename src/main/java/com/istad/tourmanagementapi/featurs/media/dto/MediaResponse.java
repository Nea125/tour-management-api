package com.istad.tourmanagementapi.featurs.media.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MediaResponse(
        Integer id,
        String name,
        String extension,
        String mediaType,
        Long size,
        String measurement,
        String uri,
        LocalDateTime createdAt
) {
}