package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingParticipantRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.BookingParticipantResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/participants")
@RequiredArgsConstructor
public class BookingParticipantController {

    private final BookingParticipantService bookingParticipantService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BookingParticipantResponse> create(@RequestBody BookingParticipantRequest request) {
        return ApiResponse.<BookingParticipantResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Booking participant created successfully")
                .data(bookingParticipantService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingParticipantResponse> findById(@PathVariable Long id) {
        return ApiResponse.<BookingParticipantResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Booking participant retrieved successfully")
                .data(bookingParticipantService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookingParticipantResponse> result = bookingParticipantService.findAll(page, size);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Booking participants retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BookingParticipantResponse> update(@PathVariable Long id,
                                                          @RequestBody BookingParticipantRequest request) {
        return ApiResponse.<BookingParticipantResponse>builder()
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
