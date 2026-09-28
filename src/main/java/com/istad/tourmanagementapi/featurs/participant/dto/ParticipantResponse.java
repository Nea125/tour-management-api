package com.istad.tourmanagementapi.featurs.participant.dto;

import java.time.LocalDate;

public record ParticipantResponse(
        Long id,
        Long bookingId,
        String fullName,
        String gender,
        LocalDate dateOfBirth,
        String phone,
        String email
) {
}
