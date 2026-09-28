package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import com.istad.tourmanagementapi.featurs.booking.dto.CreateBookingRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.UpdateBookingRequest;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.booking.mapper.BookingMapper;
import com.istad.tourmanagementapi.featurs.enums.BookingStatus;
import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.profile.UserProfileRepository;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import com.istad.tourmanagementapi.featurs.security.AuthUtils;
import com.istad.tourmanagementapi.featurs.tour_schedule.TourScheduleRepository;
import com.istad.tourmanagementapi.featurs.tour_schedule.entity.TourSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserProfileRepository userRepository;
    private final TourScheduleRepository scheduleRepository;

    // CREATE BOOKING
    @Override
    @Transactional
    public BookingResponse create(CreateBookingRequest request) {

        // Get schedule
        TourSchedule schedule =
                getScheduleById(request.scheduleId());

        // Validate schedule
        validateSchedule(schedule);

        // Validate available seats
        validateAvailability(schedule, request.numberOfPeople());
        // 4. Get current user
        String userId = AuthUtils.extractUserId();

        UserProfile user = getUserById(userId);

        // Calculate total price
        BigDecimal totalPrice = schedule.getTour().getPrice()
                        .multiply(
                                BigDecimal.valueOf(request.numberOfPeople())
                        );

        //  Create booking
        Booking booking = Booking.builder().bookingCode(
                                generateBookingCode()).user(user).schedule(schedule)
                        .numberOfPeople(
                                request.numberOfPeople()
                        )
                        .totalPrice(totalPrice)
                        .bookingDate(LocalDate.now()
                        ).status(BookingStatus.PENDING).build();

        //  Save booking
        Booking savedBooking = bookingRepository.save(booking);



        // Return booking + payment
        return new BookingResponse(
                savedBooking.getId(),
                savedBooking.getBookingCode(),
                savedBooking.getNumberOfPeople(),
                savedBooking.getTotalPrice(),
                savedBooking.getBookingDate(),
                savedBooking.getStatus(),
                savedBooking.getUser().getId(),
                savedBooking.getSchedule().getId()
        );
    }



    // FIND BY ID

    @Override
    @Transactional(readOnly = true)
    public BookingResponse findById(Long id) {
        return bookingMapper.toResponse(getById(id));
    }


    // FIND MY BOOKINGS

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> findMyBookings() {

        String userId = AuthUtils.extractUserId();

        return bookingRepository
                .findAllByUser_Id(userId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }



    // FIND ALL

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> findAll(int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return bookingRepository
                .findAll(pageable)
                .map(bookingMapper::toResponse);
    }


    // UPDATE

    @Override
    @Transactional
    public BookingResponse update(Long id, UpdateBookingRequest request
    ) {

        Booking booking = getById(id);
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Completed booking cannot be updated"
            );
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Confirmed booking cannot be updated"
            );
        }

        bookingMapper.updateEntity(
                request,
                booking
        );

        Booking updated =
                bookingRepository.save(
                        booking
                );

        return bookingMapper.toResponse(
                updated
        );
    }

    // CANCEL
    @Override
    @Transactional
    public BookingResponse cancel(Long id) {

        Booking booking = getById(id);

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Completed booking cannot be cancelled"
            );
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Booking is already cancelled"
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking cancelled = bookingRepository.save(booking);

        return bookingMapper.toResponse(cancelled);
    }

    // GET BOOKING

    private Booking getById(Long id) {

        return bookingRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Booking not found with id: " + id
                        )
                );
    }



    // GET USER

    private UserProfile getUserById(String id) {

        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found with id: " + id
                        )
                );
    }


    // GET SCHEDULE

    private TourSchedule getScheduleById(Long id) {

        return scheduleRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Schedule not found with id: " + id
                        )
                );
    }



    // VALIDATE SCHEDULE

    private void validateSchedule(
            TourSchedule schedule
    ) {

        if (schedule.getStatus() != TourScheduleStatus.OPEN) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tour schedule is not open for booking"
            );
        }
    }



    // VALIDATE AVAILABILITY

    private void validateAvailability(
            TourSchedule schedule,
            Integer numberOfPeople
    ) {

        if (
                numberOfPeople == null ||
                        numberOfPeople <= 0
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Number of people must be greater than 0"
            );
        }

        Integer maxParticipants =
                schedule.getTour()
                        .getMaxParticipants();

        Long bookedParticipants =
                bookingRepository.sumBookedPeople(
                        schedule.getId(),
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED
                        )
                );

        long remaining =
                maxParticipants - bookedParticipants;

        if (numberOfPeople > remaining) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Not enough available seats"
            );
        }
    }


    // GENERATE BOOKING CODE

    private String generateBookingCode() {

        return "BK-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}