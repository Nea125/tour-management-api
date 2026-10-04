package com.istad.tourmanagementapi.featurs.payment.dto;

public record CanPayResponse(
        boolean canPay,
        String message
) {
}