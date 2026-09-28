package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import com.istad.tourmanagementapi.featurs.booking.dto.CreateBookingRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.UpdateBookingRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingService {

    BookingResponse create(CreateBookingRequest request);

    BookingResponse findById(Long id);

    List<BookingResponse> findMyBookings();

    Page<BookingResponse> findAll(int page, int size);

    BookingResponse update(Long id, UpdateBookingRequest request);

    BookingResponse cancel(Long id);

}