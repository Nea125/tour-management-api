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
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {



    // Get bookings by status
    Page<Booking> findAllByStatus(BookingStatus status, Pageable pageable);
    // All bookings of one schedule sort by DESC date
    List<Booking> findAllBySchedule_IdOrderByBookingDateDesc(Long scheduleId);
    // Get all bookings belonging to a specific user sort by DESC date
    List<Booking> findAllByUser_IdOrderByBookingDateDesc(String userId);

    // Get total number of paid people for a specific schedule
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


    //Get booking counts for all statuses in a single query.
//    SELECT b.status, COUNT(b.id) returns the status and its booking count.
//    GROUP BY b.status puts bookings with the same status into one group.
//    ORDER BY b.status sorts the groups by their status values.
    @Query("""
        SELECT b.status, COUNT(b.id)
        FROM Booking b
        GROUP BY b.status
        ORDER BY b.status ASC
    """)
    List<Object[]> countBookingsGroupByStatus();


    // Get top performing tours based on paid bookings
    // Also calculate total revenue for each tour
    @Query("""
        SELECT new com.istad.tourmanagementapi.featurs.report.dto.TopTourResponse(
            t.id,
            t.title,
            COUNT(b.id),
            0.0,
            COALESCE(SUM(b.totalPrice), 0)
        )
        FROM Booking b
        JOIN b.schedule s
        JOIN s.tour t
        WHERE b.status = com.istad.tourmanagementapi.featurs.enums.BookingStatus.PAID
        GROUP BY t.id, t.title
        ORDER BY COUNT(b.id) DESC
    """)
    List<TopTourResponse> findTopPerformingTours(
            Pageable pageable
    );


    // Get top destinations based on paid bookings
    @Query("""
        SELECT new com.istad.tourmanagementapi.featurs.report.dto.TopDestinationResponse(
            d.id,
            d.name,
            COUNT(b.id)
        )
        FROM Booking b
        JOIN b.schedule s
        JOIN s.tour t
        JOIN t.destination d
        WHERE b.status = com.istad.tourmanagementapi.featurs.enums.BookingStatus.PAID
        GROUP BY d.id, d.name
        ORDER BY COUNT(b.id) DESC
    """)
    List<TopDestinationResponse> findTopDestinations(
            Pageable pageable
    );

}