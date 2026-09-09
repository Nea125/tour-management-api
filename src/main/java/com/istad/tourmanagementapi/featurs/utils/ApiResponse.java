package com.istad.tourmanagementapi.featurs.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
// To make field response in order
@JsonPropertyOrder({
        "status",
        "message",
        "data",
        "pagination"
})
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;
    // Check if page is null not response [when create]
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PageResponse pagination;
}