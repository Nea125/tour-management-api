package com.istad.tourmanagementapi.featurs.booking.dto;

import com.istad.tourmanagementapi.featurs.enums.BookingStatus;

import java.time.LocalDate;

public record BookingRequest(
        String bookingCode,
        String userId,
        String scheduleId,
        Integer numberOfPeople,
        LocalDate bookingDate,
        BookingStatus status,
        String specialRequest
) {
}
