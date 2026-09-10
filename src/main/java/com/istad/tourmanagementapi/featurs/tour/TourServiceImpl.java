package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.destination.DestinationRepository;
import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.tour.dto.TourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import com.istad.tourmanagementapi.featurs.tour.mapper.TourMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final DestinationRepository destinationRepository;
    private final TourMapper tourMapper;

    @Override
    public TourResponse create(TourRequest request) {
        Tour tour = tourMapper.toEntity(request);
        tour.setIsDeleted(false);
        tour.setDestination(getDestinationById(request.destinationId()));
        return tourMapper.toResponse(tourRepository.save(tour));
    }

    @Override
    public TourResponse findById(Long id) {
        return tourMapper.toResponse(getById(id));
    }

    @Override
    public Page<TourResponse> findAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return tourRepository.findByIsDeletedFalse(pageable)
                .map(tourMapper::toResponse);
    }

    @Override
    public Page<TourResponse> search(String title, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return tourRepository
                .findByIsDeletedFalseAndTitleContainingIgnoreCase(
                        title,
                        pageable
                )
                .map(tourMapper::toResponse);
    }



    @Override
    public TourResponse update(Long id, TourRequest request) {
        Tour tour = getById(id);
        tourMapper.updateEntity(request, tour);
        if (request.destinationId() != null) {
            tour.setDestination(getDestinationById(request.destinationId()));
        }
        return tourMapper.toResponse(tourRepository.save(tour));
    }

    @Override
    public void delete(Long id) {
        Tour tour = getById(id);
        tour.setIsDeleted(true);
        tourRepository.save(tour);
    }

    private Tour getById(Long id) {
        return tourRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tour not found with id: " + id
                ));
    }



    private Destination getDestinationById(Long id) {
        return destinationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Destination not found with id: " + id));
    }
}
