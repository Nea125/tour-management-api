package com.istad.tourmanagementapi.featurs.profile.dto;

import com.istad.tourmanagementapi.featurs.enums.UserStatus;

import java.time.LocalDate;
import java.util.List;

public record UserProfileResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String profileImage,
        String gender,
        LocalDate dateOfBirth,
        Boolean isDeleted,
        UserStatus status,
        String role

) {
}