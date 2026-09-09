package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.dto.DestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DestinationResponse> create(@RequestBody DestinationRequest request) {
        return ApiResponse.<DestinationResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Destination created successfully")
                .data(destinationService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DestinationResponse> findById(@PathVariable Long id) {
        return ApiResponse.<DestinationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Destination retrieved successfully")
                .data(destinationService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DestinationResponse> result = destinationService.findAll(page, size);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Destinations retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<DestinationResponse> update(@PathVariable Long id,
                                                   @RequestBody DestinationRequest request) {
        return ApiResponse.<DestinationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Destination updated successfully")
                .data(destinationService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        destinationService.delete(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Destination deleted successfully")
                .build();
    }
}
