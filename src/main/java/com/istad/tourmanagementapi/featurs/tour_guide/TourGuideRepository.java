package com.istad.tourmanagementapi.featurs.tour_guide;

import com.istad.tourmanagementapi.featurs.enums.TourGuideStatus;
import com.istad.tourmanagementapi.featurs.tour_guide.entity.TourGuide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourGuideRepository
        extends JpaRepository<TourGuide, Long> {

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumberAndIdNot(
            String licenseNumber,
            Long id
    );

    Optional<TourGuide> findByUser_Id(Long userId);

    boolean existsByUser_Id(String userId);

    Optional<TourGuide> findByIdAndStatus(
            Long id,
            TourGuideStatus status
    );

    Page<TourGuide> findAllByStatus(
            TourGuideStatus status,
            Pageable pageable
    );
}