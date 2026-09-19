package com.istad.tourmanagementapi.featurs.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
public record RegisterRequest(
        @NotBlank(message = "field userName is required")
        String userName,
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email
        @NotBlank
        String email,

        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone must be a valid number")
        String phone,

        String gender,

        LocalDate dateOfBirth,

        @NotBlank
        @Size(min = 6, message = "Password must be at least 8 characters")
        String password,

        @NotBlank
        String confirmPassword

) {
}