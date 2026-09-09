package com.istad.tourmanagementapi.featurs.schedule;

import com.istad.tourmanagementapi.featurs.schedule.dto.ScheduleRequest;
import com.istad.tourmanagementapi.featurs.schedule.dto.ScheduleResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ScheduleResponse> create(@RequestBody ScheduleRequest request) {
        return ApiResponse.<ScheduleResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Schedule created successfully")
                .data(scheduleService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ScheduleResponse> findById(@PathVariable Long id) {
        return ApiResponse.<ScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Schedule retrieved successfully")
                .data(scheduleService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ScheduleResponse> result = scheduleService.findAll(page, size);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Schedules retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ScheduleResponse> update(@PathVariable Long id,
                                                @RequestBody ScheduleRequest request) {
        return ApiResponse.<ScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Schedule updated successfully")
                .data(scheduleService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Schedule deleted successfully")
                .build();
    }
}
