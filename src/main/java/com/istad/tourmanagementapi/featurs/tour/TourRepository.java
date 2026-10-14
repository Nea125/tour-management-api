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


  // FIND POPULAR TOUR BASE ON RATTING AND REVIEW
    @Query("""
    SELECT t, AVG(r.rating), COUNT(r.id)
    FROM Tour t
    JOIN t.reviews r
    WHERE t.isDeleted = false
    AND r.isDeleted = false
    GROUP BY t
    HAVING AVG(r.rating) > :minAvgRating
       AND COUNT(r.id) >= :minReviews
    ORDER BY AVG(r.rating) DESC, COUNT(r.id) DESC
""")
    List<Object[]> findPopularTours(
            @Param("minAvgRating") double minAvgRating,
            @Param("minReviews") long minReviews
    );
}