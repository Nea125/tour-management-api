package com.istad.tourmanagementapi.featurs.report;

import com.istad.tourmanagementapi.featurs.report.dto.*;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ApiResponse<ReportResponse> getSummary() {

        return ApiResponse
                .<ReportResponse>builder()
                .status(1)
                .message("Report summary retrieved successfully")
                .data(reportService.getReport())
                .build();
    }
    @GetMapping("/bookings/status")
    public ApiResponse<List<BookingStatusReportResponse>> getBookingsByStatus() {

        return ApiResponse
                .<List<BookingStatusReportResponse>>builder()
                .status(1)
                .message("Booking statistics retrieved successfully")
                .data(reportService.getBookingsByStatus())
                .build();
    }

    @GetMapping("/tours/top")
    public ApiResponse<List<TopTourResponse>> getTopTours(
            @RequestParam(defaultValue = "5") int limit
    ) {

        return ApiResponse
                .<List<TopTourResponse>>builder()
                .status(1)
                .message("Top performing tours retrieved successfully")
                .data(reportService.getTopTours(limit))
                .build();
    }

    @GetMapping("/destinations/top")
    public ApiResponse<List<TopDestinationResponse>> getTopDestinations(
            @RequestParam(defaultValue = "5") int limit
    ) {

        return ApiResponse
                .<List<TopDestinationResponse>>builder()
                .status(1)
                .message("Top destinations retrieved successfully")
                .data(reportService.getTopDestinations(limit))
                .build();
    }
}