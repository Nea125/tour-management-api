package com.istad.tourmanagementapi.featurs.tour_schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateTourScheduleRequest(
        Long tourId,
        @NotNull(message = "Start date is required")
        LocalDate startDate,
        @NotNull(message = "Start date is required")
        LocalDate endDate,
        @NotNull(message = "Capacity is required")
        Integer capacity



) {
}