package com.istad.tourmanagementapi.featurs.profile.dto;

import com.istad.tourmanagementapi.featurs.enums.UserRole;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record CreateUserProfileRequest(
        @NotBlank
        String userName,

        @NotBlank
        String password,

        @NotBlank
        String confirmPassword,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email
        @NotBlank
        String email,

        @Pattern(
                regexp = "^\\+?[0-9]{7,15}$",
                message = "phone must be a valid number"
        )
        String phone,

        String gender,

        @Past
        LocalDate dateOfBirth,
        @NotNull
        UserRole role
) {}