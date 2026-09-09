package com.istad.tourmanagementapi.featurs.schedule.dto;

import com.istad.tourmanagementapi.featurs.enums.ScheduleStatus;

import java.time.LocalDate;

public record ScheduleRequest(
        Long tourId,
        LocalDate startDate,
        LocalDate endDate,
        Integer capacity,
        ScheduleStatus status
) {
}
