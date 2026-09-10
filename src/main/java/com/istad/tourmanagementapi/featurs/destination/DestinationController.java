package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.dto.DestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                .status(1)
                .message("Destination created successfully")
                .data(destinationService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DestinationResponse> findById(@PathVariable Long id) {
        return ApiResponse.<DestinationResponse>builder()
                .status(1)
                .message("Destination retrieved successfully")
                .data(destinationService.findById(id))
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<?> search(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<DestinationResponse> result =
                destinationService.search(name, page, size);

        return ApiResponse.builder()
                .status(1)
                .message("Destinations searched successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }


    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DestinationResponse> result = destinationService.findAll(page, size);
        return ApiResponse.builder()
                .status(1)
                .message("Destinations retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<DestinationResponse> update(@PathVariable Long id,
                                                   @RequestBody DestinationRequest request) {
        return ApiResponse.<DestinationResponse>builder()
                .status(1)
                .message("Destination updated successfully")
                .data(destinationService.update(id, request))
                .build();
    }

    @PutMapping("/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        destinationService.delete(id);
        return ApiResponse.<Void>builder()
                .status(1)
                .message("Destination deleted successfully")
                .build();
    }
}
