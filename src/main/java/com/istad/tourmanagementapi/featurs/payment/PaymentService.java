package com.istad.tourmanagementapi.featurs.payment;

import com.istad.tourmanagementapi.featurs.payment.dto.CanPayResponse;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentRequest;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    CanPayResponse canPayBooking(Long bookingId);
}