package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    boolean existsByNameIgnoreCase(String name);
    Page<Destination> findByIsDeletedFalse(Pageable pageable);

    Optional<Destination> findByIdAndIsDeletedFalse(Long id);
    Page<Destination> findByIsDeletedFalseAndNameContainingIgnoreCase(
            String title,
            Pageable pageable
    );
}
