package com.istad.tourmanagementapi.featurs.profile;

import com.istad.tourmanagementapi.featurs.profile.dto.PatchUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.UserProfileResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserProfileResponse> create(
            @Valid @RequestBody PatchUserProfileRequest request
    ) {
        return ApiResponse.<UserProfileResponse>builder()
                .status(1)
                .message("User created successfully")
                .data(userService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserProfileResponse> findById(
            @PathVariable String id
    ) {
        return ApiResponse.<UserProfileResponse>builder()
                .status(1)
                .message("User retrieved successfully")
                .data(userService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<UserProfileResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<UserProfileResponse> users =
                userService.findAll(page, size);

        String message = users.getItems().isEmpty()
                ? "No users found"
                : "Users retrieved successfully";

        return ApiResponse.<PageResponse<UserProfileResponse>>builder()
                .status(1)
                .message(message)
                .data(users)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserProfileResponse> update(
            @PathVariable String id,
            @Valid @RequestBody PatchUserProfileRequest request
    ) {
        return ApiResponse.<UserProfileResponse>builder()
                .status(1)
                .message("User updated successfully")
                .data(userService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable String id
    ) {
        userService.delete(id);

        return ApiResponse.<Void>builder()
                .status(1)
                .message("User deleted successfully")
                .build();
    }
}