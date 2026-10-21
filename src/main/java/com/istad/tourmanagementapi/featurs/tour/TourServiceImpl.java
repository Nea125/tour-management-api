package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.destination.DestinationRepository;
import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.media.MediaService;
import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.CreateTourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.PopularTourResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.UpdateTourRequest;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import com.istad.tourmanagementapi.featurs.tour.entity.TourMedia;
import com.istad.tourmanagementapi.featurs.tour.mapper.TourMapper;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.annotations.Status;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final DestinationRepository destinationRepository;
    private final TourMapper tourMapper;
    private final MediaService mediaService;

    @ResponseStatus(HttpStatus.CREATED)
    @Override
    @Transactional

    // CREATE TOUR
    public TourResponse create(
            CreateTourRequest request,
            List<MultipartFile> images
    ) {
// Check if no image exists
        if (images == null || images.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one image is required"
            );
        }

        Tour tour = tourMapper.toEntity(request);
        tour.setIsDeleted(false);
        tour.setDestination(getDestinationById(request.destinationId())
        );
        List<Media> medias = mediaService.uploadMediaEntities(images);

        // Save images in sort order
        List<TourMedia> tourImages = new ArrayList<>();
        for (int i = 0; i < medias.size(); i++) {
            TourMedia tourMedia = new TourMedia();
            tourMedia.setTour(tour);
            tourMedia.setMedia(medias.get(i));
            tourMedia.setSortOrder(i + 1);
            tourImages.add(tourMedia);
        }
        tour.setImages(tourImages);

        return tourMapper.toResponse(tourRepository.save(tour));
    }

    // FIND BY ID
    @Override
    public TourResponse findById(Long id) {
        return tourMapper.toResponse(getById(id));
    }

    @Override
    public PageResponse<TourResponse> findAll(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<TourResponse> tours =
                tourRepository.findByIsDeletedFalse(pageable).map(tourMapper::toResponse);

        return PageResponse.<TourResponse>builder()
                .items(tours.getContent())
                .size(tours.getSize())
                .pageNumber(tours.getNumber())
                .totalElements(tours.getTotalElements())
                .totalPages(tours.getTotalPages())
                .build();
    }

    // SEARCH TOURS BY TITLE
    @Override
    public PageResponse<TourResponse> search(
            String title,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TourResponse> tours = tourRepository.findByIsDeletedFalseAndTitleContainingIgnoreCase(
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


    // UPDATE TOUR
    @Override
    @Transactional
    public TourResponse update(
            Long id,
            UpdateTourRequest request,
            List<Integer> imageIds,
            List<MultipartFile> images
    ) {

        Tour tour = getById(id);

        tourMapper.updateEntity(request, tour);

        if (request.destinationId() != null) {
            tour.setDestination(
                    getDestinationById(
                            request.destinationId()
                    )
            );
        }

        // Replace images, keeping the same position
        if (images != null && !images.isEmpty()) {

            if (imageIds == null || imageIds.size() != images.size()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "imageIds and images must have the same size"
                );
            }

            Set<Integer> owned = tour.getImages().stream()
                    .map(tm -> tm.getMedia().getId())
                    .collect(Collectors.toSet());

            Set<Integer> seen = new HashSet<>();

            for (Integer imageId : imageIds) {
                if (!owned.contains(imageId)) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Image " + imageId + " does not belong to this tour"
                    );
                }
                if (!seen.add(imageId)) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Duplicate imageId: " + imageId
                    );
                }
            }

            // Only the file content changes; the TourMedia row
            // (and its sortOrder) is untouched, so the position stays the same
            for (int i = 0; i < imageIds.size(); i++) {
                mediaService.updateMedia(imageIds.get(i), images.get(i));
            }
        }

        return tourMapper.toResponse(
                tourRepository.save(tour)
        );
    }

    // DELETE TOUR
    @Override
    public void delete(Long id) {
        Tour tour = getById(id);
        tour.setIsDeleted(true);
        tourRepository.save(tour);
    }

    // GET TOUR BY ID
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

    // GET DESTINATION BY ID
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

    // GET TOURS BY DESTINATION ID
    @Override
    public PageResponse<TourResponse> findByDestinationId(
            Long destinationId,
            int page,
            int size
    ) {

        // Make sure destination exists
        getDestinationById(destinationId);

        Pageable pageable = PageRequest.of(page, size);
        Page<TourResponse> tours = tourRepository.findByDestinationIdAndIsDeletedFalse(destinationId, pageable)
                        .map(tourMapper::toResponse);

        return PageResponse.<TourResponse>builder()
                .items(tours.getContent())
                .size(tours.getSize())
                .pageNumber(tours.getNumber())
                .totalElements(tours.getTotalElements())
                .totalPages(tours.getTotalPages())
                .build();
    }


    // GET TOUR IMAGE BY ID
    @Override
    @Transactional
    public ResponseEntity<Resource> getTourImage(
            Long tourId,
            Integer imageId
    ) {
        Media media = tourRepository
                .findMediaByTourIdAndImageId(tourId, imageId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Image not found for this tour"
                        )
                );

        Resource resource = mediaService.getMediaResource(media.getId());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getMediaType())
                )
                .body(resource);
    }

    // UPDATE TOUR IMAGE
    @Override
    @Transactional
    public MediaResponse updateTourImage(
            Long tourId,
            Integer imageId,
            MultipartFile image
    ) {
        Media media = tourRepository.findMediaByTourIdAndImageId(tourId, imageId)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Image not found for this tour"
                        )
                );
        return mediaService.updateMedia(media.getId(), image
        );
    }




    private static final double POPULAR_MIN_AVG_RATING = 2.0;
    private static final long POPULAR_MIN_REVIEWS = 2;

    // GET POPULAR TOURS
    @Override
    @Transactional
    public List<PopularTourResponse> findPopularTours() {

        return tourRepository
                .findPopularTours(POPULAR_MIN_AVG_RATING, POPULAR_MIN_REVIEWS)
                .stream()
                .map(row -> new PopularTourResponse(
                        tourMapper.toResponse((Tour) row[0]),
                        Math.round(((Number) row[1]).doubleValue() * 10.0) / 10.0,
                        ((Number) row[2]).longValue()
                ))
                .toList();
    }
}