package com.istad.tourmanagementapi.featurs.payment.mapper;

import com.istad.tourmanagementapi.featurs.payment.dto.PaymentResponse;
import com.istad.tourmanagementapi.featurs.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(
            target = "bookingId",
            source = "booking.id"
    )
    PaymentResponse toResponse(Payment payment);
}