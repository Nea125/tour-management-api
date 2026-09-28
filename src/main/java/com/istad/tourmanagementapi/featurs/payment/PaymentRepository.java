package com.istad.tourmanagementapi.featurs.payment;

import com.istad.tourmanagementapi.featurs.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    boolean existsByBooking_Id(Long bookingId);
}