package com.istad.tourmanagementapi.featurs.tour_schedule.dto;


import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;

import java.time.LocalDate;

public record TourScheduleResponse(
        Long id,
        Long tourId,
        LocalDate startDate,
        LocalDate endDate,
        Integer capacity,
        TourScheduleStatus status,
        boolean isDeleted
) {
}
