package com.istad.tourmanagementapi.featurs.itinernary;

import com.istad.tourmanagementapi.featurs.itinernary.dto.ItineraryRequest;
import com.istad.tourmanagementapi.featurs.itinernary.dto.ItineraryResponse;
import org.springframework.data.domain.Page;

public interface ItineraryService {

    ItineraryResponse create(ItineraryRequest request);

    ItineraryResponse findById(Long id);

    Page<ItineraryResponse> findAll(int page, int size);

    ItineraryResponse update(Long id, ItineraryRequest request);

    void delete(Long id);
}
