package com.istad.tourmanagementapi.featurs.tour;


import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.CreateTourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.UpdateTourRequest;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TourService {

    TourResponse create(
            CreateTourRequest request,
            List<MultipartFile> images
    );

    TourResponse findById(Long id);

    PageResponse<TourResponse> findAll(
            int page,
            int size
    );

    PageResponse<TourResponse> search(
            String title,
            int page,
            int size
    );

    TourResponse update(
            Long id,
            UpdateTourRequest request
    );

    void delete(Long id);

    PageResponse<TourResponse> findByDestinationId(
            Long destinationId,
            int page,
            int size
    );

    ResponseEntity<Resource> getTourImage(
            Long tourId,
            Integer imageId
    );

    MediaResponse updateTourImage(
            Long tourId,
            Integer imageId,
            MultipartFile image
    );
}