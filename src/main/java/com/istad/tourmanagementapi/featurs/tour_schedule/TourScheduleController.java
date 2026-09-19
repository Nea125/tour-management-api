package com.istad.tourmanagementapi.featurs.tour_schedule;

import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideResponse;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.CreateTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.PatchTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.TourScheduleResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tour-schedule")
@RequiredArgsConstructor
public class TourScheduleController {

    private final TourScheduleService scheduleService;


    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TourScheduleResponse> create(
            @Valid @RequestBody CreateTourScheduleRequest request
    ) {
        return ApiResponse.<TourScheduleResponse>builder()
                .status(1)
                .message("Schedule created successfully")
                .data(scheduleService.create(request))
                .build();
    }


    // FIND BY STATUS
    @GetMapping("/status/{status}")
    public ApiResponse<PageResponse<TourScheduleResponse>> findByStatus(
            @PathVariable TourScheduleStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<TourScheduleResponse> schedules =
                scheduleService.findByStatus(
                        status,
                        page,
                        size
                );

        String message = schedules.getItems().isEmpty()
                ? "No schedules found with status: " + status
                : "Schedules retrieved successfully";

        return ApiResponse.<PageResponse<TourScheduleResponse>>builder()
                .status(1)
                .message(message)
                .data(schedules)
                .build();
    }


    // FIND BY ID
    @GetMapping("/{id}")
    public ApiResponse<TourScheduleResponse> findById(
            @PathVariable String id
    ) {
        return ApiResponse.<TourScheduleResponse>builder()
                .status(1)
                .message("Schedule retrieved successfully")
                .data(scheduleService.findById(id))
                .build();
    }


    // FIND ALL
    @GetMapping
    public ApiResponse<PageResponse<TourScheduleResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<TourScheduleResponse> schedules =
                scheduleService.findAll(page, size);

        String message = schedules.getItems().isEmpty()
                ? "No schedules found"
                : "Schedules retrieved successfully";

        return ApiResponse.<PageResponse<TourScheduleResponse>>builder()
                .status(1)
                .message(message)
                .data(schedules)
                .build();
    }


    // UPDATE
    @PatchMapping("/{id}")
    public ApiResponse<TourScheduleResponse> update(
            @PathVariable String id,
            @Valid @RequestBody PatchTourScheduleRequest request
    ) {
        return ApiResponse.<TourScheduleResponse>builder()
                .status(1)
                .message("Schedule updated successfully")
                .data(scheduleService.update(id, request))
                .build();
    }


    // SOFT DELETE
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable String id
    ) {

        scheduleService.delete(id);

        return ApiResponse.<Void>builder()
                .status(1)
                .message("Schedule deleted successfully")
                .build();
    }


    // ASSIGN GUIDE
    @PostMapping("/{scheduleId}/guides/{guideId}")
    public ApiResponse<Void> assignGuide(
            @PathVariable String scheduleId,
            @PathVariable Long guideId
    ) {

        scheduleService.assignGuide(
                scheduleId,
                guideId
        );

        return ApiResponse.<Void>builder()
                .status(1)
                .message("Guide assigned successfully")
                .build();
    }


    // UNASSIGN GUIDE
    @DeleteMapping("/{scheduleId}/guides/{guideId}")
    public ApiResponse<Void> unassignGuide(
            @PathVariable String scheduleId,
            @PathVariable Long guideId
    ) {

        scheduleService.unassignGuide(
                scheduleId,
                guideId
        );

        return ApiResponse.<Void>builder()
                .status(1)
                .message("Guide unassigned successfully")
                .build();
    }

    @GetMapping("/tour/{tourId}")
    public ApiResponse<PageResponse<TourScheduleResponse>> findByTourId(
            @PathVariable Long tourId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<TourScheduleResponse>>builder()
                .status(1)
                .message("Schedules retrieved successfully")
                .data(
                        scheduleService.findByTourId(
                                tourId,
                                page,
                                size
                        )
                )
                .build();
    }
// Find guides by schedule ID
    @GetMapping("/{scheduleId}/guides")
    public ApiResponse<List<TourGuideResponse>> findGuidesByScheduleId(
            @PathVariable String scheduleId
    ) {
        return ApiResponse.<List<TourGuideResponse>>builder()
                .status(1)
                .message("Tour guides retrieved successfully")
                .data(scheduleService.findGuidesByScheduleId(scheduleId))
                .build();
    }
}