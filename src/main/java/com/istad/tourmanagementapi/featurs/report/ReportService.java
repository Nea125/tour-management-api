package com.istad.tourmanagementapi.featurs.report;

import com.istad.tourmanagementapi.featurs.report.dto.*;

import java.util.List;

public interface ReportService {

    ReportResponse getReport();

    List<BookingStatusReportResponse> getBookingsByStatus();

    List<TopTourResponse> getTopTours(int limit);

    List<TopDestinationResponse> getTopDestinations(int limit);

}