package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import org.springframework.data.domain.Page;

public interface BookingService {

    BookingResponse create(BookingRequest request);

    BookingResponse findById(Long id);

    Page<BookingResponse> findAll(int page, int size);

    BookingResponse update(Long id, BookingRequest request);

    void delete(Long id);
}
