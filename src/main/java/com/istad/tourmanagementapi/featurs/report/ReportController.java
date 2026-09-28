package com.istad.tourmanagementapi.featurs.report;

import com.istad.tourmanagementapi.featurs.report.dto.ReportResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    @GetMapping
    public ApiResponse<ReportResponse> getReport() {

        return ApiResponse.<ReportResponse>builder()
                .status(1)
                .message("Report retrieved successfully")
                .data(reportService.getReport())
                .build();
    }
}
