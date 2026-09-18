package com.istad.tourmanagementapi.featurs.profile.dto;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
public record PatchUserProfileRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String profileImage,
        String gender,
        LocalDate dateOfBirth
) {
}
