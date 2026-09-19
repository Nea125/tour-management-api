package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.dto.CreateDestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.destination.dto.UpdateDestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.destination.mapper.DestinationMapper;
import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.media.MediaService;
import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl
        implements DestinationService {

    private final DestinationRepository destinationRepository;

    private final DestinationMapper destinationMapper;

    private final MediaService mediaService;


    @Override
    @Transactional
    public DestinationResponse create(
            CreateDestinationRequest request,
            List<MultipartFile> images
    ) {

        // Check duplicate destination
        if (destinationRepository.existsByNameIgnoreCase(
                request.name()
        )) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Destination already exists with name: "
                            + request.name()
            );
        }

        // At least one image is required
        if (images == null || images.isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one image is required"
            );
        }

        // Map request to entity
        Destination destination =
                destinationMapper.toEntity(request);

        destination.setDeleted(false);

        // Upload images
        List<Media> medias =
                mediaService.uploadMediaEntities(images);

        // Connect images to destination
        destination.setMedia(medias);

        // Save destination
        Destination saved =
                destinationRepository.save(destination);

        return destinationMapper.toResponse(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public DestinationResponse findById(Long id) {

        Destination destination = getById(id);

        return destinationMapper.toResponse(destination);
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponse<DestinationResponse> findAll(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<DestinationResponse> destinations =
                destinationRepository
                        .findByIsDeletedFalse(pageable)
                        .map(destinationMapper::toResponse);

        return PageResponse.<DestinationResponse>builder()
                .items(destinations.getContent())
                .size(destinations.getSize())
                .pageNumber(destinations.getNumber())
                .totalElements(destinations.getTotalElements())
                .totalPages(destinations.getTotalPages())
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponse<DestinationResponse> search(
            String name,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<DestinationResponse> destinations =
                destinationRepository
                        .findByIsDeletedFalseAndNameContainingIgnoreCase(
                                name,
                                pageable
                        )
                        .map(destinationMapper::toResponse);

        return PageResponse.<DestinationResponse>builder()
                .items(destinations.getContent())
                .size(destinations.getSize())
                .pageNumber(destinations.getNumber())
                .totalElements(destinations.getTotalElements())
                .totalPages(destinations.getTotalPages())
                .build();
    }


    @Override
    @Transactional
    public DestinationResponse update(
            Long id,
            UpdateDestinationRequest request
    ) {

        Destination destination = getById(id);

        destinationMapper.updateEntity(
                request,
                destination
        );

        return destinationMapper.toResponse(destination);
    }


    @Override
    @Transactional
    public void delete(Long id) {

        Destination destination =
                getById(id);

        // Soft delete
        destination.setDeleted(true);

        destinationRepository.save(destination);
    }


    private Destination getById(Long id) {

        return destinationRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Destination not found with id: "
                                        + id
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> getDestinationImage(
            Long destinationId,
            Integer imageId
    ) {

        Media media = destinationRepository
                .findMediaByDestinationIdAndImageId(
                        destinationId,
                        imageId
                )
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Image not found for this destination"
                        )
                );

        Resource resource =
                mediaService.getMediaResource(media.getId());

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(media.getMediaType())
                )
                .body(resource);
    }

    @Override
    @Transactional
    public MediaResponse updateDestinationImage(
            Long destinationId,
            Integer imageId,
            MultipartFile image
    ) {

        Media media = destinationRepository
                .findMediaByDestinationIdAndImageId(
                        destinationId,
                        imageId
                )
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Image not found for this destination"
                        )
                );

        return mediaService.updateMedia(
                media.getId(),
                image
        );
    }
}