package com.istad.tourmanagementapi.featurs.participant;
import com.istad.tourmanagementapi.featurs.booking.BookingRepository;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.participant.dto.ParticipantRequest;
import com.istad.tourmanagementapi.featurs.participant.dto.ParticipantResponse;
import com.istad.tourmanagementapi.featurs.participant.entity.Participant;
import com.istad.tourmanagementapi.featurs.participant.mapper.ParticipantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository bookingParticipantRepository;
    private final ParticipantMapper bookingParticipantMapper;
    private final BookingRepository bookingRepository;

    @Override
    public ParticipantResponse create(ParticipantRequest request) {
        Participant participant = bookingParticipantMapper.toEntity(request);
        participant.setBooking(getBookingById(request.bookingId()));
        return bookingParticipantMapper.toResponse(bookingParticipantRepository.save(participant));
    }

    @Override
    public ParticipantResponse findById(Long id) {
        return bookingParticipantMapper.toResponse(getById(id));
    }

    @Override
    public Page<ParticipantResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingParticipantRepository.findAll(pageable).map(bookingParticipantMapper::toResponse);
    }

    @Override
    public ParticipantResponse update(Long id, ParticipantRequest request) {
        Participant participant = getById(id);
        bookingParticipantMapper.updateEntity(request, participant);
        if (request.bookingId() != null) {
            participant.setBooking(getBookingById(request.bookingId()));
        }
        return bookingParticipantMapper.toResponse(bookingParticipantRepository.save(participant));
    }

    @Override
    public void delete(Long id) {
        Participant participant = getById(id);
        bookingParticipantRepository.delete(participant);
    }

    private Participant getById(Long id) {
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
