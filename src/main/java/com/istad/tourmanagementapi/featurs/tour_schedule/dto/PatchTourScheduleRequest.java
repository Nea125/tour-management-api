package com.istad.tourmanagementapi.featurs.tour_schedule.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatchTourScheduleRequest(

        LocalDate startDate,

        LocalDate endDate,
        @Min(
                value = 1,
                message = "Capacity must be at least 1"
        )

        Integer capacity



) {
}