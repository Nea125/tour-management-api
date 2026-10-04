package com.istad.tourmanagementapi.featurs.payment;

import com.istad.tourmanagementapi.featurs.booking.BookingRepository;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.enums.BookingStatus;
import com.istad.tourmanagementapi.featurs.enums.PaymentStatus;
import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.payment.dto.CanPayResponse;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentRequest;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentResponse;
import com.istad.tourmanagementapi.featurs.payment.entity.Payment;
import com.istad.tourmanagementapi.featurs.payment.mapper.PaymentMapper;
import com.istad.tourmanagementapi.featurs.tour_schedule.TourScheduleRepository;
import com.istad.tourmanagementapi.featurs.tour_schedule.entity.TourSchedule;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;
    private final TourScheduleRepository tourScheduleRepository;

    @Override
    @Transactional
    public PaymentResponse pay(PaymentRequest request) {

        Booking booking =
                bookingRepository.findById(request.bookingId())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Booking not found"
                                )
                        );

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Booking is not pending"
            );
        }

        if (paymentRepository.existsByBooking_Id(
                booking.getId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Booking has already been paid"
            );
        }

        // Check available capacity before payment
        validateCapacity(booking);

        // Simulate payment
        Payment payment = Payment.builder()
                .paymentCode(generatePaymentCode())
                .amount(booking.getTotalPrice())
                .currency("USD")
                .status(PaymentStatus.SUCCESS)
                .paymentDate(LocalDateTime.now())
                .booking(booking)
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        // Payment successful
        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);


        updateScheduleStatus(booking.getSchedule());

        return paymentMapper.toResponse(savedPayment);
    }

    private void validateCapacity(Booking booking) {

        TourSchedule schedule =
                booking.getSchedule();

        Integer bookedPeople =
                bookingRepository.countPaidPeopleByScheduleId(
                        schedule.getId()
                );

        int availableCapacity =
                schedule.getCapacity() - bookedPeople;

        if (availableCapacity <= 0) {

            schedule.setStatus(TourScheduleStatus.FULL);

            tourScheduleRepository.save(schedule);

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tour schedule is fully booked"
            );
        }

        if (booking.getNumberOfPeople() > availableCapacity) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Not enough available seats. Remaining seats: "
                            + availableCapacity
            );
        }
    }

    private void updateScheduleStatus(
            TourSchedule schedule
    ) {

        Integer bookedPeople =
                bookingRepository.countPaidPeopleByScheduleId(
                        schedule.getId()
                );

        if (bookedPeople >= schedule.getCapacity()) {
            schedule.setStatus(
                    TourScheduleStatus.FULL
            );
        } else {
            schedule.setStatus(
                    TourScheduleStatus.OPEN
            );
        }

        tourScheduleRepository.save(schedule);
    }


    private String generatePaymentCode() {
        return "PAY-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    @Override
    public CanPayResponse canPay(Long bookingId) {

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Booking not found"
                                )
                        );

        if (booking.getStatus() != BookingStatus.PENDING) {
            return new CanPayResponse(
                    false,
                    "Booking is not pending"
            );
        }

        if (paymentRepository.existsByBooking_Id(bookingId)) {
            return new CanPayResponse(
                    false,
                    "This booking has already been paid"
            );
        }

        TourSchedule schedule =
                booking.getSchedule();

        if (schedule.getStatus() != TourScheduleStatus.OPEN) {
            return new CanPayResponse(
                    false,
                    "Tour schedule is not available for payment"
            );
        }

        LocalDate today = LocalDate.now();

        if (schedule.getStartDate().isBefore(today)) {
            return new CanPayResponse(
                    false,
                    "Payment is no longer available because the tour has already started"
            );
        }

        Integer bookedPeople =
                bookingRepository.countPaidPeopleByScheduleId(
                        schedule.getId()
                );

        int availableCapacity =
                Math.max(
                        0,
                        schedule.getCapacity() - bookedPeople
                );

        if (availableCapacity <= 0) {

            schedule.setStatus(
                    TourScheduleStatus.FULL
            );

            tourScheduleRepository.save(schedule);

            return new CanPayResponse(
                    false,
                    "Tour schedule is fully booked"
            );
        }

        if (booking.getNumberOfPeople() > availableCapacity) {
            return new CanPayResponse(
                    false,
                    "Not enough available seats. Remaining seats: "
                            + availableCapacity
            );
        }

        return new CanPayResponse(
                true,
                "Payment is available"
        );
    }
}