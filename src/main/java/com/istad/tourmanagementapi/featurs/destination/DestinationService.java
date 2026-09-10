package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.dto.DestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import org.springframework.data.domain.Page;

public interface DestinationService {

    DestinationResponse create(DestinationRequest request);

    DestinationResponse findById(Long id);

    Page<DestinationResponse> findAll(int page, int size);

    Page<DestinationResponse> search(String title, int page, int size);

    DestinationResponse update(Long id, DestinationRequest request);

    void delete(Long id);
}
