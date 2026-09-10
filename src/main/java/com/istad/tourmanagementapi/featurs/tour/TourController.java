package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.tour.dto.TourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TourResponse> create(@RequestBody TourRequest request) {
        return ApiResponse.<TourResponse>builder()
                .status(1)
                .message("Tour created successfully")
                .data(tourService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<TourResponse> findById(@PathVariable Long id) {
        return ApiResponse.<TourResponse>builder()
                .status(1)
                .message("Tour retrieved successfully")
                .data(tourService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TourResponse> result =
                tourService.findAll(page, size);

        return ApiResponse.builder()
                .status(1)
                .message("Tours retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<?> search(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TourResponse> result =
                tourService.search(title, page, size);

        return ApiResponse.builder()
                .status(1)
                .message("Tours searched successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<TourResponse> update(@PathVariable Long id,
                                            @RequestBody TourRequest request) {
        return ApiResponse.<TourResponse>builder()
                .status(1)
                .message("Tour updated successfully")
                .data(tourService.update(id, request))
                .build();
    }

    @PutMapping("/{id}/update")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        tourService.delete(id);
        return ApiResponse.<Void>builder()
                .status(1)
                .message("Tour deleted successfully")
                .build();
    }
}
