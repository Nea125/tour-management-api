package com.istad.tourmanagementapi.featurs.booking.dto;

import java.time.LocalDate;

public record BookingParticipantResponse(
        Long id,
        Long bookingId,
        String fullName,
        String gender,
        LocalDate dateOfBirth,
        String phone,
        String email
) {
}
