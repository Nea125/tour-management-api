package com.istad.tourmanagementapi.featurs.report;

import com.istad.tourmanagementapi.featurs.booking.BookingRepository;
import com.istad.tourmanagementapi.featurs.destination.DestinationRepository;
import com.istad.tourmanagementapi.featurs.enums.*;
import com.istad.tourmanagementapi.featurs.profile.UserProfileRepository;
import com.istad.tourmanagementapi.featurs.report.ReportService;
import com.istad.tourmanagementapi.featurs.report.dto.*;
import com.istad.tourmanagementapi.featurs.review.ReviewRepository;
import com.istad.tourmanagementapi.featurs.tour.TourRepository;
import com.istad.tourmanagementapi.featurs.tour_guide.TourGuideRepository;
import com.istad.tourmanagementapi.featurs.tour_schedule.TourScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final DestinationRepository destinationRepository;
    private final TourGuideRepository tourGuideRepository;
    private final UserProfileRepository userProfileRepository;
    private final ReviewRepository tourReviewRepository;
    private final TourScheduleRepository tourScheduleRepository;

    @Override
    public ReportResponse getReport() {
        LocalDate today = LocalDate.now();
        BigDecimal totalRevenue =
                bookingRepository.getTotalRevenueByStatus(
                        BookingStatus.PAID
                );

        Long totalBooking =
                bookingRepository.countTotalBookings();

        Long totalTour =
                tourRepository.countTotalTours();

        Long totalDestination =
                destinationRepository.countTotalDestinations();

        Double averageRating =
                tourReviewRepository.getAverageRating();

        Long totalCustomer =
                userProfileRepository.countUsersByRoleAndStatus(
                        UserRole.CUSTOMER,
                        UserStatus.ACTIVE
                );

        Long totalGuide =
                tourGuideRepository.countGuidesByStatus(
                        TourGuideStatus.ACTIVE
                );


        // Upcoming tour count by next day not today
        Long upcomingTour =
                tourScheduleRepository.countUpcomingTours(
                        today,
                        today.plusDays(3)
                );

        Long completedTour =
                tourScheduleRepository.countToursByStatus(
                        TourScheduleStatus.COMPLETED
                );

        Long cancelledBooking =
                bookingRepository.countBookingsByStatus(
                        BookingStatus.CANCELLED
                );

        return new ReportResponse(
                totalRevenue,
                totalBooking,
                totalTour,
                totalDestination,
                averageRating,
                totalCustomer,
                totalGuide,
                upcomingTour,
                completedTour,
                cancelledBooking
        );
    }

    @Override
    public List<TopTourResponse> getTopTours(int limit) {

        PageRequest pageable =
                PageRequest.of(0, limit);

        return bookingRepository.findTopPerformingTours(
                pageable
        );
    }

    @Override
    public List<TopDestinationResponse> getTopDestinations(
            int limit
    ) {

        PageRequest pageable =
                PageRequest.of(0, limit);

        return bookingRepository.findTopDestinations(
                pageable
        );
    }

    @Override
    public List<BookingStatusReportResponse> getBookingsByStatus() {

        List<Object[]> results =
                bookingRepository.countBookingsGroupByStatus();

        List<BookingStatusReportResponse> response =
                new ArrayList<>();

        for (Object[] result : results) {

            BookingStatus status =
                    (BookingStatus) result[0];

            Long total =
                    (Long) result[1];

            response.add(
                    new BookingStatusReportResponse(
                            status,
                            total
                    )
            );
        }

        return response;
    }



}