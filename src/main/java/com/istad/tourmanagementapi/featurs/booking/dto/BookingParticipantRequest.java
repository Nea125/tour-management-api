package com.istad.tourmanagementapi.featurs.booking.dto;

import java.time.LocalDate;

public record BookingParticipantRequest(
        Long bookingId,
        String fullName,
        String gender,
        LocalDate dateOfBirth,
        String phone,
        String email
) {
}
