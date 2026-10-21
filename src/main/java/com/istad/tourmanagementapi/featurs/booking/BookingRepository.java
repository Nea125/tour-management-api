package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.enums.BookingStatus;
import com.istad.tourmanagementapi.featurs.report.dto.TopDestinationResponse;
import com.istad.tourmanagementapi.featurs.report.dto.TopTourResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

//    // Get all bookings with a specific status
    Page<Booking> findAllByStatus(BookingStatus status, Pageable pageable);
//
//    // Get all bookings belonging to a specific schedule sort by DESC date
//    List<Booking> findAllBySchedule_IdOrderByBookingDateDesc(Long scheduleId);
//
//   // Get all bookings belonging to a specific user sort by DESC date
//    List<Booking> findAllByUser_IdOrderByBookingDateDesc(String userId);


    // Get all bookings belonging to a specific schedule, latest booking first
    List<Booking> findAllBySchedule_IdOrderByBookingDateDescIdDesc(Long scheduleId);

    // Get all bookings belonging to a specific user, latest booking first
    List<Booking> findAllByUser_IdOrderByBookingDateDescIdDesc(String userId);

    // Get total number of people booked for a specific schedule
    @Query("""
        SELECT COALESCE(SUM(b.numberOfPeople), 0)
        FROM Booking b
        WHERE b.schedule.id = :scheduleId
          AND b.status = com.istad.tourmanagementapi.featurs.enums.BookingStatus.PAID
    """)
    Integer countPaidPeopleByScheduleId(
            @Param("scheduleId") Long scheduleId
    );


    // Get total revenue for bookings with a specific status
    @Query("""
        SELECT COALESCE(SUM(b.totalPrice), 0)
        FROM Booking b
        WHERE b.status = :status
    """)
    BigDecimal getTotalRevenueByStatus(
            @Param("status") BookingStatus status
    );


    // Get total number of bookings
    @Query("""
        SELECT COALESCE(COUNT(b),0)
        FROM Booking b
    """)
    Long countTotalBookings();


    // Get number of bookings with a specific status
    @Query("""
        SELECT COUNT(b)
        FROM Booking b
        WHERE b.status = :status
    """)
    Long countBookingsByStatus(
            @Param("status") BookingStatus status
    );


// Get number of bookings grouped by status
    // return field status and total
//SELECT status as status, COUNT(bookings.status) as total
//    FROM bookings
//    GROUP BY status;
    @Query("""
        SELECT booking.status as status, COUNT(booking.status) as total
        FROM Booking booking
        GROUP BY booking.status
    """)
    List<Object[]> countBookingsGroupByStatus();

    // Get top-performing tours based on paid bookings
//    SELECT
//    t.id AS id,
//    t.title AS title,
//    COUNT(b.id) AS total_booking,
//    COALESCE(
//            (
//            SELECT AVG(r.rating)
//    FROM reviews r
//    WHERE r.tour_id = t.id
//    AND r.is_deleted = FALSE
//            ),
//                    0.0
//                    ) AS average_rating,
//    COALESCE(SUM(b.total_price), 0) AS total_revenue
//    FROM bookings b
//    JOIN tour_schedules s ON b.tour_schedule_id = s.id
//    JOIN tours t ON s.tour_id = t.id
//    WHERE b.status = 'PAID'
//    GROUP BY t.id, t.title
//    ORDER BY SUM(b.total_price) DESC, t.id ASC
//    LIMIT 10;

    @Query("""
    SELECT new com.istad.tourmanagementapi.featurs.report.dto.TopTourResponse(
        tour.id as id,
        tour.title as title,
        COUNT(booking) as total_booking,
        COALESCE(
            (
                SELECT AVG(review.rating)
                FROM Review review
                WHERE review.tour.id = tour.id
                  AND review.isDeleted = false
            ) ,
            0.0
        ) as average_rating,

        COALESCE(SUM(booking.totalPrice), 0) as total_revenue
    )
    FROM Booking booking
    JOIN booking.schedule schedule
    JOIN schedule.tour tour
    WHERE booking.status = com.istad.tourmanagementapi.featurs.enums.BookingStatus.PAID
    AND schedule.isDeleted=false
    AND tour.isDeleted=false
     /*
          group all paid bookings by tour ID and tour name
      */
    GROUP BY tour.id, tour.title

    ORDER BY SUM(booking.totalPrice) DESC,tour.id ASC
""")
    List<TopTourResponse> findTopPerformingTours(Pageable pageable);


    // Get top destinations based on paid bookings order it by more booking come first

//    SELECT
//    d.id AS destination_id,
//    d.name AS destination_name,
//    COUNT(b.id) AS total_booking
//    FROM bookings b
//    JOIN tour_schedules s
//    ON s.id = b.tour_schedule_id
//    JOIN tours t
//    ON t.id = s.tour_id
//    JOIN destinations d
//    ON d.id = t.destination_id
//    WHERE b.status = 'PAID'
//    AND d.is_deleted = FALSE
//    GROUP BY d.id, d.name
//    ORDER BY COUNT(b.id) DESC
//    LIMIT 10;
    @Query("""
        SELECT new com.istad.tourmanagementapi.featurs.report.dto.TopDestinationResponse(
            destination.id as destination_id,
            destination.name as destination_name,
            COUNT(booking) as total_booking
        )
        FROM Booking booking
        JOIN booking.schedule schedule
        JOIN schedule.tour tour
        JOIN tour.destination destination
 
        WHERE booking.status = com.istad.tourmanagementapi.featurs.enums.BookingStatus.PAID
        AND destination.isDeleted = false
        AND tour.isDeleted = false
        AND schedule.isDeleted = false
            /*
                group all paid bookings by destination ID and destination name
             */
        GROUP BY destination.id, destination.name
        ORDER BY COUNT(booking) DESC
    """)
    List<TopDestinationResponse> findTopDestinations(
            Pageable pageable
    );

}