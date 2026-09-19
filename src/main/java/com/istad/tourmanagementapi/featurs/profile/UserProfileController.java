package com.istad.tourmanagementapi.featurs.profile;


import com.istad.tourmanagementapi.featurs.profile.dto.CreateUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.PatchUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.UserProfileResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userService;
    private final UserProfileService userProfileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserProfileResponse> create(
            @Valid @RequestBody CreateUserProfileRequest request
    ) {
        return ApiResponse.<UserProfileResponse>builder()
                .status(1)
                .message("User registered successfully")
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

    @PatchMapping("/{id}")
    public ApiResponse<UserProfileResponse> update(
            @PathVariable String id,
            @Valid @RequestBody PatchUserProfileRequest request
    ) {
        return ApiResponse.<UserProfileResponse>builder()
                .status(1)
                .message("User profile updated successfully")
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

    @PatchMapping(
            value = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<UserProfileResponse> updateProfileImage(
            @PathVariable String id,
            @RequestPart("image") MultipartFile image
    ) {
        return ApiResponse.<UserProfileResponse>builder()
                .status(1)
                .message("Profile image updated successfully")
                .data(userProfileService.updateProfileImage(id, image))
                .build();
    }
}