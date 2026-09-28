package com.istad.tourmanagementapi.featurs.review;

import com.istad.tourmanagementapi.featurs.review.dto.ReviewRequest;
import com.istad.tourmanagementapi.featurs.review.dto.ReviewResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReviewResponse> create(
            @Valid @RequestBody ReviewRequest request
    ) {

        return ApiResponse.<ReviewResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Review created successfully")
                .data(reviewService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ReviewResponse> findById(
            @PathVariable Long id
    ) {

        return ApiResponse.<ReviewResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Review retrieved successfully")
                .data(reviewService.findById(id))
                .build();
    }

    @GetMapping("/booking/{bookingId}")
    public ApiResponse<ReviewResponse> findByBookingId(
            @PathVariable Long bookingId
    ) {

        return ApiResponse.<ReviewResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Review retrieved successfully")
                .data(reviewService.findByBookingId(bookingId))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id
    ) {

        reviewService.delete(id);

        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Review deleted successfully")
                .build();
    }

    @GetMapping("/tour/{tourId}")
    public ApiResponse<PageResponse<ReviewResponse>> findByTourId(
            @PathVariable Long tourId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<ReviewResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tour reviews retrieved successfully")
                .data(
                        reviewService.findByTourId(
                                tourId,
                                page,
                                size
                        )
                )
                .build();
    }


}