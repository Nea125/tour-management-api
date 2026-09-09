package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingParticipantRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.BookingParticipantResponse;
import org.springframework.data.domain.Page;

public interface BookingParticipantService {

    BookingParticipantResponse create(BookingParticipantRequest request);

    BookingParticipantResponse findById(Long id);

    Page<BookingParticipantResponse> findAll(int page, int size);

    BookingParticipantResponse update(Long id, BookingParticipantRequest request);

    void delete(Long id);
}
