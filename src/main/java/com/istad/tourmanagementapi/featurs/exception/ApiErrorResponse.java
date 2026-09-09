package com.istad.tourmanagementapi.featurs.exception;

import lombok.Builder;

@Builder
public record ApiErrorResponse<T>(
        Integer code,
        Boolean isSuccess,
        String message,
        Integer timestamp,
        T errorDetails
) {
}
