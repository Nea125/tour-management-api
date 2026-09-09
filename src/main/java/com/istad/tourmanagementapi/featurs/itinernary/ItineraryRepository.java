package com.istad.tourmanagementapi.featurs.itinernary;

import com.istad.tourmanagementapi.featurs.itinernary.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
}
