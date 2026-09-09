package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BookingResponse> create(@RequestBody BookingRequest request) {
        return ApiResponse.<BookingResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Booking created successfully")
                .data(bookingService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> findById(@PathVariable Long id) {
        return ApiResponse.<BookingResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Booking retrieved successfully")
                .data(bookingService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookingResponse> result = bookingService.findAll(page, size);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Bookings retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BookingResponse> update(@PathVariable Long id,
                                               @RequestBody BookingRequest request) {
        return ApiResponse.<BookingResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Booking updated successfully")
                .data(bookingService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Booking deleted successfully")
                .build();
    }
}
