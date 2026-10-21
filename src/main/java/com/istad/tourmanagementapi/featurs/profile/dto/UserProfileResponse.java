package com.istad.tourmanagementapi.featurs.profile.dto;

import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import org.apache.james.mime4j.dom.datetime.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String role

) {
}