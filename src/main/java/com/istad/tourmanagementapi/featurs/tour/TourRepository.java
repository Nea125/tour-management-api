package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
