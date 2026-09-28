package com.istad.tourmanagementapi.featurs.payment;
import com.istad.tourmanagementapi.featurs.booking.BookingRepository;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.enums.BookingStatus;
import com.istad.tourmanagementapi.featurs.enums.PaymentStatus;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentRequest;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentResponse;
import com.istad.tourmanagementapi.featurs.payment.entity.Payment;
import com.istad.tourmanagementapi.featurs.payment.mapper.PaymentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse pay(
            PaymentRequest request
    ) {

        Booking booking =
                bookingRepository.findById(request.bookingId()
                ).orElseThrow(() -> new ResponseStatusException(
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

        // Simulate payment
        Payment payment = Payment.builder()
                        .paymentCode(generatePaymentCode()
                        ).amount(booking.getTotalPrice()
                        ).currency("USD")
                        .status(PaymentStatus.SUCCESS)
                        .paymentDate(LocalDateTime.now()
                        ).booking(booking)
                        .build();

        Payment savedPayment = paymentRepository.save(payment);
        // Payment successful → confirm booking
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        return paymentMapper.toResponse(
                savedPayment
        );
    }

    @Override
    public PaymentResponse findByBookingId(Long bookingId) {
        return null;
    }

    private String generatePaymentCode() {

        return "PAY-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}