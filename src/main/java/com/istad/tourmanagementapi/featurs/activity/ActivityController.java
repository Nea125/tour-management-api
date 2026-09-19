package com.istad.tourmanagementapi.featurs.activity;

import com.istad.tourmanagementapi.featurs.activity.dto.ActivityRequest;
import com.istad.tourmanagementapi.featurs.activity.dto.ActivityResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;


    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ActivityResponse> create(
            @Valid @RequestBody ActivityRequest request
    ) {
        return ApiResponse.<ActivityResponse>builder()
                .status(1)
                .message("Activity created successfully")
                .data(activityService.create(request))
                .build();
    }


    // FIND BY ID
    @GetMapping("/{id}")
    public ApiResponse<ActivityResponse> findById(
            @PathVariable Long id
    ) {
        return ApiResponse.<ActivityResponse>builder()
                .status(1)
                .message("Activity retrieved successfully")
                .data(activityService.findById(id))
                .build();
    }


    // FIND ALL
    @GetMapping
    public ApiResponse<PageResponse<ActivityResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<ActivityResponse> activities =
                activityService.findAll(page, size);

        String message = activities.getItems().isEmpty()
                ? "No activities found"
                : "Activities retrieved successfully";

        return ApiResponse.<PageResponse<ActivityResponse>>builder()
                .status(1)
                .message(message)
                .data(activities)
                .build();
    }


    // UPDATE
    @PutMapping("/{id}")
    public ApiResponse<ActivityResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActivityRequest request
    ) {
        return ApiResponse.<ActivityResponse>builder()
                .status(1)
                .message("Activity updated successfully")
                .data(activityService.update(id, request))
                .build();
    }


    // SOFT DELETE
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id
    ) {

        activityService.delete(id);

        return ApiResponse.<Void>builder()
                .status(1)
                .message("Activity deleted successfully")
                .build();
    }

    @GetMapping("/tour/{tourId}")
    public ApiResponse<PageResponse<ActivityResponse>> findByTourId(
            @PathVariable Long tourId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<ActivityResponse>>builder()
                .status(1)
                .message("Activities retrieved successfully")
                .data(
                        activityService.findByTourId(
                                tourId,
                                page,
                                size
                        )
                )
                .build();
    }
}