package com.istad.tourmanagementapi.featurs.report;

import com.istad.tourmanagementapi.featurs.booking.BookingRepository;
import com.istad.tourmanagementapi.featurs.destination.DestinationRepository;
import com.istad.tourmanagementapi.featurs.enums.BookingStatus;
import com.istad.tourmanagementapi.featurs.enums.TourGuideStatus;
import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.enums.UserRole;
import com.istad.tourmanagementapi.featurs.profile.UserProfileRepository;
import com.istad.tourmanagementapi.featurs.report.ReportService;
import com.istad.tourmanagementapi.featurs.report.dto.ReportResponse;
import com.istad.tourmanagementapi.featurs.review.ReviewRepository;
import com.istad.tourmanagementapi.featurs.tour.TourRepository;
import com.istad.tourmanagementapi.featurs.tour_guide.TourGuideRepository;
import com.istad.tourmanagementapi.featurs.tour_schedule.TourScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

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
                        BookingStatus.CONFIRMED
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
                userProfileRepository.countUsersByRole(
                        UserRole.CUSTOMER
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
}