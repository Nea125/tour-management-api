package com.istad.tourmanagementapi.featurs.payment;

import com.istad.tourmanagementapi.featurs.payment.dto.CanPayResponse;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentRequest;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse pay(PaymentRequest request);

    CanPayResponse canPay(Long bookingId);
}