package com.istad.tourmanagementapi.featurs.participant;

import com.istad.tourmanagementapi.featurs.participant.dto.ParticipantRequest;
import com.istad.tourmanagementapi.featurs.participant.dto.ParticipantResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService bookingParticipantService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ParticipantResponse> create(@RequestBody ParticipantRequest request) {
        return ApiResponse.<ParticipantResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Booking participant created successfully")
                .data(bookingParticipantService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ParticipantResponse> findById(@PathVariable Long id) {
        return ApiResponse.<ParticipantResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Booking participant retrieved successfully")
                .data(bookingParticipantService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ParticipantResponse> result = bookingParticipantService.findAll(page, size);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Booking participants retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ParticipantResponse> update(@PathVariable Long id,
                                                   @RequestBody ParticipantRequest request) {
        return ApiResponse.<ParticipantResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Booking participant updated successfully")
                .data(bookingParticipantService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        bookingParticipantService.delete(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Booking participant deleted successfully")
                .build();
    }
}
