package com.istad.tourmanagementapi.featurs.review;

import com.istad.tourmanagementapi.featurs.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    boolean existsByBooking_Id(Long bookingId);

    Optional<Review> findByBooking_Id(Long bookingId);

    @Query("""
    SELECT r
    FROM Review r
    WHERE r.tour.id = :tourId
""")
    Page<Review> findByTourId(
            @Param("tourId") Long tourId,
            Pageable pageable
    );

    @Query("""
    SELECT COALESCE(AVG(r.rating), 0)
    FROM Review r
""")
    Double getAverageRating();
}