package com.istad.tourmanagementapi.featurs.auth.dto;

import java.time.LocalDate;

public record RegisterResponse(
        String id,
        String userName,
        String firstName,
        String lastName,
        String email,
        String phone,
        String gender,
        LocalDate dateOfBirth
) {
}