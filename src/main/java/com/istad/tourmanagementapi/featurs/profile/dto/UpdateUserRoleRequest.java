package com.istad.tourmanagementapi.featurs.profile.dto;

import com.istad.tourmanagementapi.featurs.enums.UserRole;
import jakarta.validation.constraints.NotNull;


public record UpdateUserRoleRequest(
        @NotNull(message = "Role is required")
        UserRole role
) {
}