package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import com.istad.tourmanagementapi.featurs.booking.dto.CreateBookingRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.UpdateBookingRequest;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {

    BookingResponse create(CreateBookingRequest request);

    BookingResponse findById(Long id);

    Page<BookingResponse> findByStatus(BookingStatus status, int page, int size);
    List<BookingResponse> findByScheduleId(Long scheduleId);
    List<BookingResponse> findMyBookings();

    Page<BookingResponse> findAll(int page, int size);

    BookingResponse update(Long id, UpdateBookingRequest request);

    BookingResponse cancel(Long id);

}