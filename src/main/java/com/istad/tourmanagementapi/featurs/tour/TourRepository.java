package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {
    Page<Tour> findByIsDeletedFalse(Pageable pageable);
    Optional<Tour> findByIdAndIsDeletedFalse(Long id);
    Page<Tour> findByIsDeletedFalseAndTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );

}
