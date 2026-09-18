package com.istad.tourmanagementapi.featurs.tour_guide;

import com.istad.tourmanagementapi.featurs.tour_guide.dto.PatchTourGuideRequest;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideRequest;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tour-guide")
@RequiredArgsConstructor
public class TourGuideController {

    private final TourGuideService guideService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TourGuideResponse> create(
            @Valid @RequestBody TourGuideRequest request
    ) {
        return ApiResponse.<TourGuideResponse>builder()
                .status(1)
                .message("Tour guide created successfully")
                .data(guideService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<TourGuideResponse> findById(
            @PathVariable Long id
    ) {
        return ApiResponse.<TourGuideResponse>builder()
                .status(1)
                .message("Guide retrieved successfully")
                .data(guideService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<TourGuideResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<TourGuideResponse> guides =
                guideService.findAll(page, size);

        String message = guides.getItems().isEmpty()
                ? "No tour guides found"
                : "Tour guides retrieved successfully";

        return ApiResponse.<PageResponse<TourGuideResponse>>builder()
                .status(1)
                .message(message)
                .data(guides)
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<TourGuideResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PatchTourGuideRequest request
    ) {
        return ApiResponse.<TourGuideResponse>builder()
                .status(1)
                .message("Tour guide updated successfully")
                .data(guideService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id
    ) {
        guideService.delete(id);

        return ApiResponse.<Void>builder()
                .status(1)
                .message("Guide deleted successfully")
                .build();
    }
}