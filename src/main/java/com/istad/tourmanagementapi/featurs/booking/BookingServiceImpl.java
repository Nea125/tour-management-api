package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.schedule.ScheduleRepository;
import com.istad.tourmanagementapi.featurs.schedule.entity.Schedule;
import com.istad.tourmanagementapi.featurs.user.UserRepository;
import com.istad.tourmanagementapi.featurs.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public BookingResponse create(BookingRequest request) {

        Booking booking = bookingMapper.toEntity(request);

        User user = getUserById(request.userId());
        Schedule schedule = getScheduleById(request.scheduleId());

        booking.setUser(user);
        booking.setSchedule(schedule);

        return bookingMapper.toResponse(
                bookingRepository.save(booking)
        );
    }

    @Override
    public BookingResponse findById(Long id) {
        return bookingMapper.toResponse(getById(id));
    }

    @Override
    public Page<BookingResponse> findAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return bookingRepository
                .findAll(pageable)
                .map(bookingMapper::toResponse);
    }

    @Override
    public BookingResponse update(Long id, BookingRequest request) {

        Booking booking = getById(id);

        bookingMapper.updateEntity(request, booking);

        if (request.userId() != null) {
            booking.setUser(
                    getUserById(request.userId())
            );
        }

        if (request.scheduleId() != null) {
            booking.setSchedule(
                    getScheduleById(request.scheduleId())
            );
        }

        return bookingMapper.toResponse(
                bookingRepository.save(booking)
        );
    }

    @Override
    public void delete(Long id) {

        Booking booking = getById(id);

        bookingRepository.delete(booking);
    }

    private Booking getById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Booking not found with id: " + id
                        )
                );
    }

    private User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found with id: " + id
                        )
                );
    }

    private Schedule getScheduleById(Long id) {

        return scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Schedule not found with id: " + id
                        )
                );
    }
}