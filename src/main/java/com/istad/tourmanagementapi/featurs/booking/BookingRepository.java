package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        SELECT COALESCE(
            SUM(b.numberOfPeople),
            0
        )
        FROM Booking b
        WHERE b.schedule.id = :scheduleId
          AND b.status IN :statuses
    """)
    Long sumBookedPeople(
            @Param("scheduleId") Long scheduleId,
            @Param("statuses") Collection<BookingStatus> statuses
    );

    List<Booking> findAllByUser_Id(String userId);

    @Query("""
    SELECT COALESCE(SUM(b.numberOfPeople), 0)
    FROM Booking b
    WHERE b.schedule.id = :scheduleId
      AND b.status = com.istad.tourmanagementapi.featurs.enums.BookingStatus.CONFIRMED
""")
    Integer countConfirmedPeopleByScheduleId(
            @Param("scheduleId") Long scheduleId
    );

    @Query("""
    SELECT COALESCE(SUM(b.totalPrice), 0)
    FROM Booking b
    WHERE b.status = :status
""")
    BigDecimal getTotalRevenueByStatus(
            @Param("status") BookingStatus status
    );

    @Query("""
    SELECT COUNT(b)
    FROM Booking b
""")
    Long countTotalBookings();

    @Query("""
    SELECT COUNT(b)
    FROM Booking b
    WHERE b.status = :status
""")
    Long countBookingsByStatus(
            @Param("status") BookingStatus status
    );
}