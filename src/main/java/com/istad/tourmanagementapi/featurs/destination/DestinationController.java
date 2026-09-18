package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.dto.DestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DestinationResponse> create(
            @Valid @RequestBody DestinationRequest request
    ) {
        return ApiResponse.<DestinationResponse>builder()
                .status(1)
                .message("Destination created successfully")
                .data(destinationService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DestinationResponse> findById(
            @PathVariable Long id
    ) {
        return ApiResponse.<DestinationResponse>builder()
                .status(1)
                .message("Destination retrieved successfully")
                .data(destinationService.findById(id))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<DestinationResponse>> search(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<DestinationResponse> destinations =
                destinationService.search(name, page, size);

        String message = destinations.getItems().isEmpty()
                ? "No destinations found"
                : "Destinations retrieved successfully";

        return ApiResponse.<PageResponse<DestinationResponse>>builder()
                .status(1)
                .message(message)
                .data(destinations)
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<DestinationResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<DestinationResponse> destinations =
                destinationService.findAll(page, size);

        String message = destinations.getItems().isEmpty()
                ? "No destinations found"
                : "Destinations retrieved successfully";

        return ApiResponse.<PageResponse<DestinationResponse>>builder()
                .status(1)
                .message(message)
                .data(destinations)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<DestinationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DestinationRequest request
    ) {
        return ApiResponse.<DestinationResponse>builder()
                .status(1)
                .message("Destination updated successfully")
                .data(destinationService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id
    ) {
        destinationService.delete(id);

        return ApiResponse.<Void>builder()
                .status(1)
                .message("Destination deleted successfully")
                .build();
    }
}