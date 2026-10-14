package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.media.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface DestinationRepository
        extends JpaRepository<Destination, Long> {

    boolean existsByNameIgnoreCase(String name);

    Page<Destination> findByIsDeletedFalse(Pageable pageable);

    Optional<Destination> findByIdAndIsDeletedFalse(Long id);

    Page<Destination> findByIsDeletedFalseAndNameContainingIgnoreCase(String name, Pageable pageable);

    // GET DESTINATION IMAGE BY ID
    @Query("""
    SELECT dm.media
    FROM DestinationMedia dm
    WHERE dm.destination.id = :destinationId
    AND dm.media.id = :imageId
    AND dm.destination.isDeleted = false
    """)
    Optional<Media> findMediaByDestinationIdAndImageId(
            @Param("destinationId") Long destinationId,
            @Param("imageId") Integer imageId
    );
// COUNT TOTAL DESTINATION
    @Query("""
    SELECT COUNT(d)
    FROM Destination d
    WHERE d.isDeleted = false
""")
    Long countTotalDestinations();

}