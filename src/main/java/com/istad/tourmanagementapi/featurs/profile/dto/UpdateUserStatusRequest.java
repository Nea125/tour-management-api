package com.istad.tourmanagementapi.featurs.profile.dto;
import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
public record UpdateUserStatusRequest(
        @NotNull(message = "Status is required")
        UserStatus status
) {
}