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

    boolean existsByBooking_IdAndIsDeletedFalse(Long bookingId);

    Optional<Review> findByBooking_IdAndIsDeletedFalse(Long bookingId);

    Page<Review> findByTour_IdAndIsDeletedFalse(
            Long tourId,
            Pageable pageable
    );

    Page<Review> findByUser_IdAndIsDeletedFalse(
            String userId,
            Pageable pageable
    );

    // Query to get the average rating
    // Get the average rating from all non-deleted reviews
    // If there are no reviews, return 0
    // COALESCE returns 0 when AVG(r.rating) is null
    // Example: AVG(6, 3) = 4.5
    @Query("""
           SELECT COALESCE(AVG(r.rating), 0)
           FROM Review r
           WHERE r.isDeleted = false
           """)
    Double getAverageRating();
}