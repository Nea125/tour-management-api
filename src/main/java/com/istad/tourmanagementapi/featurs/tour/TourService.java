package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.tour.dto.TourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import org.springframework.data.domain.Page;

public interface TourService {

    TourResponse create(TourRequest request);

    TourResponse findById(Long id);

    Page<TourResponse> findAll(int page, int size);
    Page<TourResponse> search(String title, int page, int size);
    TourResponse update(Long id, TourRequest request);

    void delete(Long id);
}
