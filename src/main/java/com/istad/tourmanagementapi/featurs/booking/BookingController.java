package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import com.istad.tourmanagementapi.featurs.booking.dto.CreateBookingRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.UpdateBookingRequest;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final PageMapper pageMapper;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BookingResponse> create(
            @Valid
            @RequestBody CreateBookingRequest request
    ) {

        return ApiResponse
                .<BookingResponse>builder()
                .status(1)
                .message("Booking created successfully")
                .data(bookingService.create(request)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> findById(
            @PathVariable Long id
    ) {

        return ApiResponse
                .<BookingResponse>builder()
                .status(1)
                .message("Booking retrieved successfully")
                .data(bookingService.findById(id))
                .build();
    }

    @GetMapping("/my")
    public ApiResponse<List<BookingResponse>> findMyBookings() {

        return ApiResponse
                .<List<BookingResponse>>builder()
                .status(1)
                .message("My bookings retrieved successfully")
                .data(bookingService.findMyBookings())
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0")
            int page, @RequestParam(defaultValue = "10") int size
    ) {

        Page<BookingResponse> result = bookingService.findAll(page, size);
        return ApiResponse.builder()
                .status(1)
                .message(
                        "Bookings retrieved successfully"
                )
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BookingResponse> update(
            @PathVariable Long id,

            @Valid
            @RequestBody UpdateBookingRequest request
    ) {

        return ApiResponse
                .<BookingResponse>builder()
                .status(1)
                .message("Booking updated successfully")
                .data(bookingService.update(id, request))
                .build();
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<BookingResponse> cancel(
            @PathVariable Long id
    ) {
        return ApiResponse
                .<BookingResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Booking cancelled successfully")
                .data(bookingService.cancel(id))
                .build();
    }
}