package com.istad.tourmanagementapi.featurs.report.dto;

import com.istad.tourmanagementapi.featurs.enums.BookingStatus;

public record BookingStatusReportResponse(
        BookingStatus status,
        Long total
) {
}