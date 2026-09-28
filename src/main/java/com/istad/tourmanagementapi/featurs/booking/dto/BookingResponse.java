package com.istad.tourmanagementapi.featurs.booking.dto;

import com.istad.tourmanagementapi.featurs.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingResponse(

        Long id,
        String bookingCode,
        Integer numberOfPeople,
        BigDecimal totalPrice,
        LocalDate bookingDate,
        BookingStatus status,
        String userId,
        Long scheduleId

) {
}