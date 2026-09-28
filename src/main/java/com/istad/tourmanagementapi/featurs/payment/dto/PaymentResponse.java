package com.istad.tourmanagementapi.featurs.payment.dto;

import com.istad.tourmanagementapi.featurs.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String paymentCode,
        Long bookingId,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        LocalDateTime paymentDate
) {
}