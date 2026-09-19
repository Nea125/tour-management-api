package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
    SELECT m
    FROM Tour t
    JOIN t.media m
    WHERE t.id = :tourId
    AND m.id = :imageId
    AND t.isDeleted = false
    """)
    Optional<Media> findMediaByTourIdAndImageId(
            @Param("tourId") Long tourId,
            @Param("imageId") Integer imageId
    );
}