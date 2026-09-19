package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.tour.dto.TourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;

public interface TourService {

    TourResponse create(TourRequest request);

    TourResponse findById(Long id);

    PageResponse<TourResponse> findAll(int page, int size);

    PageResponse<TourResponse> search(
            String title,
            int page,
            int size
    );

    TourResponse update(Long id, TourRequest request);

    void delete(Long id);

    PageResponse<TourResponse> findByDestinationId(
            Long destinationId,
            int page,
            int size
    );
}