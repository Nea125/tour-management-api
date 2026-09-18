package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.dto.DestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.destination.mapper.DestinationMapper;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;

    @Override
    public DestinationResponse create(DestinationRequest request) {

        if (destinationRepository.existsByNameIgnoreCase(request.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Destination already exists with name: " + request.name()
            );
        }

        Destination destination = destinationMapper.toEntity(request);
        destination.setDeleted(false);

        return destinationMapper.toResponse(
                destinationRepository.save(destination)
        );
    }

    @Override
    public DestinationResponse findById(Long id) {
        return destinationMapper.toResponse(getById(id));
    }

    @Override
    public PageResponse<DestinationResponse> findAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

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
    public PageResponse<DestinationResponse> search(
            String name,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

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
    public DestinationResponse update(
            Long id,
            DestinationRequest request
    ) {
        Destination destination = getById(id);

        destinationMapper.updateEntity(request, destination);

        return destinationMapper.toResponse(
                destinationRepository.save(destination)
        );
    }

    @Override
    public void delete(Long id) {
        Destination destination = getById(id);

        destination.setDeleted(true);

        destinationRepository.save(destination);
    }

    private Destination getById(Long id) {
        return destinationRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Destination not found with id: " + id
                        )
                );
    }
}