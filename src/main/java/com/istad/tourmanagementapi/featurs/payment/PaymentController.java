package com.istad.tourmanagementapi.featurs.payment;

import com.istad.tourmanagementapi.featurs.payment.dto.CanPayResponse;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentRequest;
import com.istad.tourmanagementapi.featurs.payment.dto.PaymentResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    // CREATE PAYMENT
    public ApiResponse<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request
    ) {

        return ApiResponse
                .<PaymentResponse>builder()
                .status(1)
                .message("Payment successful")
                .data(paymentService.createPayment(request)
                ).build();
    }

// CHECK IF PAYMENT IS POSSIBLE TO PAY
    @GetMapping("/booking/{bookingId}/can-pay")
    public ApiResponse<CanPayResponse> canPay(@PathVariable Long bookingId
    ) {

        CanPayResponse response = paymentService.canPayBooking(bookingId);
        return ApiResponse
                .<CanPayResponse>builder()
                .status(1)
                .message(response.message())
                .data(response)
                .build();
    }
}