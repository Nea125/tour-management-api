package com.istad.tourmanagementapi.featurs.Activity;

import com.istad.tourmanagementapi.featurs.Activity.dto.ActivityRequest;
import com.istad.tourmanagementapi.featurs.Activity.dto.ActivityResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService itineraryService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ActivityResponse> create(@RequestBody ActivityRequest request) {
        return ApiResponse.<ActivityResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Itinerary created successfully")
                .data(itineraryService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ActivityResponse> findById(@PathVariable Long id) {
        return ApiResponse.<ActivityResponse>builder()
                .status(1)
                .message("Itinerary retrieved successfully")
                .data(itineraryService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ActivityResponse> result = itineraryService.findAll(page, size);
        return ApiResponse.builder()
                .status(1)
                .message("Activity retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ActivityResponse> update(@PathVariable Long id,
                                                @RequestBody ActivityRequest request) {
        return ApiResponse.<ActivityResponse>builder()
                .status(1)
                .message("Activity updated successfully")
                .data(itineraryService.update(id, request))
                .build();
    }

    @PutMapping("/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        itineraryService.delete(id);
        return ApiResponse.<Void>builder()
                .status(1)
                .message("Activity deleted successfully")
                .build();
    }
}
