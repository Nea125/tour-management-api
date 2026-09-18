package com.istad.tourmanagementapi.featurs.profile.dto;

import com.istad.tourmanagementapi.featurs.enums.UserStatus;

import java.time.LocalDate;

public record UserProfileResponse(
        String  id,
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
