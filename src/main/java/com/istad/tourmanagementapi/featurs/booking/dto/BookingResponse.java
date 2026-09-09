package com.istad.tourmanagementapi.featurs.booking.dto;

import com.istad.tourmanagementapi.featurs.enums.BookingStatus;

import java.time.LocalDate;

public record BookingResponse(
        Long id,
        String bookingCode,
        Long userId,
        Long scheduleId,
        Integer numberOfPeople,
        LocalDate bookingDate,
        BookingStatus status,
        String specialRequest
) {
}
