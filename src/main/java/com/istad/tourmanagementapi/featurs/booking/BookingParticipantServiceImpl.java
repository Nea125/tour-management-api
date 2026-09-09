package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingParticipantRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.BookingParticipantResponse;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.booking.entity.BookingParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BookingParticipantServiceImpl implements BookingParticipantService {

    private final BookingParticipantRepository bookingParticipantRepository;
    private final BookingParticipantMapper bookingParticipantMapper;
    private final BookingRepository bookingRepository;

    @Override
    public BookingParticipantResponse create(BookingParticipantRequest request) {
        BookingParticipant participant = bookingParticipantMapper.toEntity(request);
        participant.setBooking(getBookingById(request.bookingId()));
        return bookingParticipantMapper.toResponse(bookingParticipantRepository.save(participant));
    }

    @Override
    public BookingParticipantResponse findById(Long id) {
        return bookingParticipantMapper.toResponse(getById(id));
    }

    @Override
    public Page<BookingParticipantResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingParticipantRepository.findAll(pageable).map(bookingParticipantMapper::toResponse);
    }

    @Override
    public BookingParticipantResponse update(Long id, BookingParticipantRequest request) {
        BookingParticipant participant = getById(id);
        bookingParticipantMapper.updateEntity(request, participant);
        if (request.bookingId() != null) {
            participant.setBooking(getBookingById(request.bookingId()));
        }
        return bookingParticipantMapper.toResponse(bookingParticipantRepository.save(participant));
    }

    @Override
    public void delete(Long id) {
        BookingParticipant participant = getById(id);
        bookingParticipantRepository.delete(participant);
    }

    private BookingParticipant getById(Long id) {
        return bookingParticipantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Booking participant not found with id: " + id));
    }

    private Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Booking not found with id: " + id));
    }
}
