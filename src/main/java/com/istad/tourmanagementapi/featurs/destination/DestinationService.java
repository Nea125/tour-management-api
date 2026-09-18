package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.dto.DestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;

public interface DestinationService {

    DestinationResponse create(DestinationRequest request);

    DestinationResponse findById(Long id);

    PageResponse<DestinationResponse> findAll(int page, int size);

    PageResponse<DestinationResponse> search(String name, int page, int size);

    DestinationResponse update(Long id, DestinationRequest request);

    void delete(Long id);
}