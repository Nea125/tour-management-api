package com.istad.tourmanagementapi.featurs.user.dto;

import com.istad.tourmanagementapi.featurs.enums.UserStatus;

import java.time.LocalDate;

public record UserRequest(
        String keycloakUserId,
        String firstName,
        String lastName,
        String email,
        String phone,
        String profileImage,
        String gender,
        LocalDate dateOfBirth,
        UserStatus status
) {
}
