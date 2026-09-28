package com.istad.tourmanagementapi.featurs.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBookingRequest(

        @NotNull
        Long scheduleId,
        @NotNull
        @Positive
        Integer numberOfPeople

) {
}