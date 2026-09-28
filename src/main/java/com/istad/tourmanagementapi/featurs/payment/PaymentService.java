package com.istad.tourmanagementapi.featurs.payment;

import com.istad.tourmanagementapi.featurs.payment.dto.PaymentRequest;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse pay(
            PaymentRequest request
    );

    PaymentResponse findByBookingId(
            Long bookingId
    );
}