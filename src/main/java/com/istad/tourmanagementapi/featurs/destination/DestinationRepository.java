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

    Page<Destination> findByIsDeletedFalse(
            Pageable pageable
    );

    Optional<Destination> findByIdAndIsDeletedFalse(
            Long id
    );

    Page<Destination>
    findByIsDeletedFalseAndNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    // Find destination media by media and destination id
    @Query("""
    SELECT m
    FROM Destination d
    JOIN d.media m
    WHERE d.id = :destinationId
    AND m.id = :imageId
    AND d.isDeleted = false
    """)
    Optional<Media> findMediaByDestinationIdAndImageId(
            @Param("destinationId") Long destinationId,
            @Param("imageId") Integer imageId
    );

}