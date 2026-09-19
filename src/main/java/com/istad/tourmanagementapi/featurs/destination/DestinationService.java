package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.dto.CreateDestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.destination.dto.UpdateDestinationRequest;
import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DestinationService {

    DestinationResponse create(
            CreateDestinationRequest request,
            List<MultipartFile> images
    );

    DestinationResponse findById(Long id);

    PageResponse<DestinationResponse> findAll(
            int page,
            int size
    );

    PageResponse<DestinationResponse> search(
            String name,
            int page,
            int size
    );

    DestinationResponse update(
            Long id,
            UpdateDestinationRequest request
    );

    void delete(Long id);

    ResponseEntity<Resource> getDestinationImage(
            Long destinationId,
            Integer imageId
    );

    MediaResponse updateDestinationImage(
            Long destinationId,
            Integer imageId,
            MultipartFile image
    );
}