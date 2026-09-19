package com.istad.tourmanagementapi.featurs.profile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

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
        LocalDate dateOfBirth
) {}