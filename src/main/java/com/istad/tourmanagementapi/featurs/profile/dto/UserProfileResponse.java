package com.istad.tourmanagementapi.featurs.profile.dto;

import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;

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
        boolean isDeleted,
        UserStatus status
) {
}
