package com.istad.tourmanagementapi.featurs.guide;

import com.istad.tourmanagementapi.featurs.guide.dto.GuideRequest;
import com.istad.tourmanagementapi.featurs.guide.dto.GuideResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guides")
@RequiredArgsConstructor
public class GuideController {

    private final GuideService guideService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<GuideResponse> create(@RequestBody GuideRequest request) {
        return ApiResponse.<GuideResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Guide created successfully")
                .data(guideService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<GuideResponse> findById(@PathVariable Long id) {
        return ApiResponse.<GuideResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Guide retrieved successfully")
                .data(guideService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<GuideResponse> result = guideService.findAll(page, size);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Guides retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<GuideResponse> update(@PathVariable Long id,
                                             @RequestBody GuideRequest request) {
        return ApiResponse.<GuideResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Guide updated successfully")
                .data(guideService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        guideService.delete(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Guide deleted successfully")
                .build();
    }
}
