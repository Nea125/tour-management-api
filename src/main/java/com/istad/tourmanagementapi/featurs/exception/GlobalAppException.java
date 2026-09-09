package com.istad.tourmanagementapi.featurs.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@ResponseStatus(HttpStatus.BAD_REQUEST) // annotation use to set status code to bad request when exception
@RestControllerAdvice // annotation use to handle all exception

public class GlobalAppException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse<?> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        List<Map<String, Object>> errorList = new ArrayList<>();
        e.getFieldErrors().forEach(fieldError -> {
            Map<String, Object> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("reason",  fieldError.getDefaultMessage());
            errorList.add(error);
        });

        return ApiErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .isSuccess(false)
                .message("Data submission have validated failed")
                .timestamp(Instant.now().getNano())
                .errorDetails(errorList)
                .build();
    }




   //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiErrorResponse<?> handleJsonException(HttpMessageNotReadableException e) {
        return ApiErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .isSuccess(false)
                .message("Data format or syntax is not correct")
                .timestamp(Instant.now().getNano())
                .errorDetails(e.getLocalizedMessage())
                .build();
    }





    // Error in service is dynamic we use ResponseEntity to handle it
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleServiceException(ResponseStatusException e) {
         return  ResponseEntity.status(e.getStatusCode()).body(ApiErrorResponse.builder()
                 .code(e.getStatusCode().value())
                 .isSuccess(false)
                 .message("Business logic error")
                 .timestamp(Instant.now().getNano())
                 .errorDetails(e.getReason())
                 .build()) ;
    }




   // Prevent data integrity violation [like when we delete parent category]
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiErrorResponse<?> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        return ApiErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .isSuccess(false)
                .message("Data integrity violation")
                .timestamp(Instant.now().getNano())
                .errorDetails(e.getLocalizedMessage())
                .build();
    }
}


