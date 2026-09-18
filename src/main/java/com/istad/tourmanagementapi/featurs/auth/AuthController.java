package com.istad.tourmanagementapi.featurs.auth;

import com.istad.tourmanagementapi.featurs.auth.dto.RegisterRequest;
import com.istad.tourmanagementapi.featurs.auth.dto.RegisterResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RegisterResponse> register(
          @Valid @RequestBody RegisterRequest registerRequest
    ) {

        RegisterResponse response = authService.register(registerRequest);

        return ApiResponse.<RegisterResponse>builder()
                .status(1)
                .message("User registered successfully")
                .data(response)
                .build();
    }
}