package com.istad.tourmanagementapi.featurs.itinernary;

import com.istad.tourmanagementapi.featurs.itinernary.dto.ItineraryRequest;
import com.istad.tourmanagementapi.featurs.itinernary.dto.ItineraryResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ItineraryResponse> create(@RequestBody ItineraryRequest request) {
        return ApiResponse.<ItineraryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Itinerary created successfully")
                .data(itineraryService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ItineraryResponse> findById(@PathVariable Long id) {
        return ApiResponse.<ItineraryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Itinerary retrieved successfully")
                .data(itineraryService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ItineraryResponse> result = itineraryService.findAll(page, size);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Itineraries retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ItineraryResponse> update(@PathVariable Long id,
                                                 @RequestBody ItineraryRequest request) {
        return ApiResponse.<ItineraryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Itinerary updated successfully")
                .data(itineraryService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        itineraryService.delete(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Itinerary deleted successfully")
                .build();
    }
}
