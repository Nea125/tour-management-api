package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.tour.dto.TourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TourResponse> create(
            @Valid @RequestBody TourRequest request
    ) {
        return ApiResponse.<TourResponse>builder()
                .status(1)
                .message("Tour created successfully")
                .data(tourService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<TourResponse> findById(
            @PathVariable Long id
    ) {
        return ApiResponse.<TourResponse>builder()
                .status(1)
                .message("Tour retrieved successfully")
                .data(tourService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<TourResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<TourResponse> tours =
                tourService.findAll(page, size);

        String message = tours.getItems().isEmpty()
                ? "No tours found"
                : "Tours retrieved successfully";

        return ApiResponse.<PageResponse<TourResponse>>builder()
                .status(1)
                .message(message)
                .data(tours)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<TourResponse>> search(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<TourResponse> tours =
                tourService.search(title, page, size);

        String message = tours.getItems().isEmpty()
                ? "No tours found"
                : "Tours retrieved successfully";

        return ApiResponse.<PageResponse<TourResponse>>builder()
                .status(1)
                .message(message)
                .data(tours)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<TourResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TourRequest request
    ) {
        return ApiResponse.<TourResponse>builder()
                .status(1)
                .message("Tour updated successfully")
                .data(tourService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id
    ) {
        tourService.delete(id);

        return ApiResponse.<Void>builder()
                .status(1)
                .message("Tour deleted successfully")
                .build();
    }

    @GetMapping("/destination/{destinationId}")
    public ApiResponse<PageResponse<TourResponse>> findByDestinationId(
            @PathVariable Long destinationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<TourResponse> tours =
                tourService.findByDestinationId(
                        destinationId,
                        page,
                        size
                );

        String message = tours.getItems().isEmpty()
                ? "No tours found for this destination"
                : "Tours retrieved successfully";

        return ApiResponse.<PageResponse<TourResponse>>builder()
                .status(1)
                .message(message)
                .data(tours)
                .build();
    }
}