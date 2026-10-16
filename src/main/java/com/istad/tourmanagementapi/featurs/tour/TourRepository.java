package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TourRepository
        extends JpaRepository<Tour, Long> {

    Page<Tour> findByIsDeletedFalse(Pageable pageable);

    Optional<Tour> findByIdAndIsDeletedFalse(Long id);

    Page<Tour> findByIsDeletedFalseAndTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );
    Page<Tour> findByDestinationIdAndIsDeletedFalse(
            Long destinationId,
            Pageable pageable
    );

    // FIND TOUR IMAGE BY TOUR ID AND IMAGE ID
    @Query("""
    SELECT tm.media
    FROM TourMedia tm
    WHERE tm.tour.id = :tourId
    AND tm.media.id = :imageId
    AND tm.tour.isDeleted = false
    """)
    Optional<Media> findMediaByTourIdAndImageId(
            @Param("tourId") Long tourId,
            @Param("imageId") Integer imageId
    );

    // COUNT TOTAL TOUR
    @Query("""
    SELECT COUNT(t)
    FROM Tour t
    WHERE t.isDeleted = false
""")
    Long countTotalTours();


    // FIND POPULAR TOURS BASED ON RATING OR REVIEW COUNT
    @Query("""
    SELECT
        tour,
        AVG(review.rating),
        COUNT(review.id)
    FROM Tour tour
    JOIN tour.reviews review
    WHERE tour.isDeleted = false
      AND review.isDeleted = false
    GROUP BY tour
    HAVING AVG(review.rating) >= :minAvgRating
        OR COUNT(review) >= :minReviews
    ORDER BY
        AVG(review.rating) DESC,
        COUNT(review) DESC
    """)
    List<Object[]> findPopularTours(
            @Param("minAvgRating") double minAvgRating,
            @Param("minReviews") long minReviews
    );
}