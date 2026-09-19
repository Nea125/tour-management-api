package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.destination.DestinationRepository;
import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.media.MediaService;
import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.CreateTourRequest;

import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.UpdateTourRequest;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import com.istad.tourmanagementapi.featurs.tour.mapper.TourMapper;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final DestinationRepository destinationRepository;
    private final TourMapper tourMapper;
    private final MediaService mediaService;

    @Override
    @Transactional
    public TourResponse create(
            CreateTourRequest request,
            List<MultipartFile> images
    ) {

        if (images == null || images.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one image is required"
            );
        }

        Tour tour = tourMapper.toEntity(request);

        tour.setIsDeleted(false);

        tour.setDestination(
                getDestinationById(
                        request.destinationId()
                )
        );

        List<Media> medias =
                mediaService.uploadMediaEntities(images);

        tour.setMedia(medias);

        return tourMapper.toResponse(
                tourRepository.save(tour)
        );
    }

    @Override
    public TourResponse findById(Long id) {
        return tourMapper.toResponse(getById(id));
    }

    @Override
    public PageResponse<TourResponse> findAll(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<TourResponse> tours =
                tourRepository
                        .findByIsDeletedFalse(pageable)
                        .map(tourMapper::toResponse);

        return PageResponse.<TourResponse>builder()
                .items(tours.getContent())
                .size(tours.getSize())
                .pageNumber(tours.getNumber())
                .totalElements(tours.getTotalElements())
                .totalPages(tours.getTotalPages())
                .build();
    }

    @Override
    public PageResponse<TourResponse> search(
            String title,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<TourResponse> tours =
                tourRepository
                        .findByIsDeletedFalseAndTitleContainingIgnoreCase(
                                title,
                                pageable
                        )
                        .map(tourMapper::toResponse);

        return PageResponse.<TourResponse>builder()
                .items(tours.getContent())
                .size(tours.getSize())
                .pageNumber(tours.getNumber())
                .totalElements(tours.getTotalElements())
                .totalPages(tours.getTotalPages())
                .build();
    }

    @Override
    public TourResponse update(
            Long id,
            UpdateTourRequest request
    ) {

        Tour tour = getById(id);

        tourMapper.updateEntity(
                request,
                tour
        );

        if (request.destinationId() != null) {
            tour.setDestination(
                    getDestinationById(
                            request.destinationId()
                    )
            );
        }

        return tourMapper.toResponse(
                tourRepository.save(tour)
        );
    }

    @Override
    public void delete(Long id) {

        Tour tour = getById(id);

        tour.setIsDeleted(true);

        tourRepository.save(tour);
    }

    private Tour getById(Long id) {

        return tourRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Tour not found with id: " + id
                        )
                );
    }

    private Destination getDestinationById(Long id) {

        return destinationRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Destination not found with id: " + id
                        )
                );
    }

    @Override
    public PageResponse<TourResponse> findByDestinationId(
            Long destinationId,
            int page,
            int size
    ) {

        // Make sure destination exists
        getDestinationById(destinationId);

        Pageable pageable = PageRequest.of(page, size);

        Page<TourResponse> tours =
                tourRepository
                        .findByDestinationIdAndIsDeletedFalse(
                                destinationId,
                                pageable
                        )
                        .map(tourMapper::toResponse);

        return PageResponse.<TourResponse>builder()
                .items(tours.getContent())
                .size(tours.getSize())
                .pageNumber(tours.getNumber())
                .totalElements(tours.getTotalElements())
                .totalPages(tours.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<Resource> getTourImage(
            Long tourId,
            Integer imageId
    ) {

        Media media = tourRepository
                .findMediaByTourIdAndImageId(
                        tourId,
                        imageId
                )
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Image not found for this tour"
                        )
                );

        Resource resource =
                mediaService.getMediaResource(
                        media.getId()
                );

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                media.getMediaType()
                        )
                )
                .body(resource);
    }

    @Override
    @Transactional
    public MediaResponse updateTourImage(
            Long tourId,
            Integer imageId,
            MultipartFile image
    ) {

        Media media = tourRepository
                .findMediaByTourIdAndImageId(
                        tourId,
                        imageId
                )
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Image not found for this tour"
                        )
                );

        return mediaService.updateMedia(
                media.getId(),
                image
        );
    }
}