package com.istad.tourmanagementapi.featurs.payment;

import com.istad.tourmanagementapi.featurs.payment.PaymentService;
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
    public ApiResponse<PaymentResponse> pay(
            @Valid
            @RequestBody PaymentRequest request
    ) {

        return ApiResponse
                .<PaymentResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Payment successful")
                .data(
                        paymentService.pay(request)
                )
                .build();
    }

    @GetMapping("/booking/{bookingId}/can-pay")
    public ApiResponse<CanPayResponse> canPay(
            @PathVariable Long bookingId
    ) {

        CanPayResponse response =
                paymentService.canPay(bookingId);

        return ApiResponse
                .<CanPayResponse>builder()
                .status(HttpStatus.OK.value())
                .message(response.message())
                .data(response)
                .build();
    }
}