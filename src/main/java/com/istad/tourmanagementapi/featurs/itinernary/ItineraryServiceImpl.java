package com.istad.tourmanagementapi.featurs.itinernary;

import com.istad.tourmanagementapi.featurs.itinernary.dto.ItineraryRequest;
import com.istad.tourmanagementapi.featurs.itinernary.dto.ItineraryResponse;
import com.istad.tourmanagementapi.featurs.itinernary.entity.Itinerary;
import com.istad.tourmanagementapi.featurs.tour.TourRepository;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TourRepository tourRepository;
    private final ItineraryMapper itineraryMapper;

    @Override
    public ItineraryResponse create(ItineraryRequest request) {
        Itinerary itinerary = itineraryMapper.toEntity(request);
        itinerary.setTour(getTourById(request.tourId()));
        return itineraryMapper.toResponse(itineraryRepository.save(itinerary));
    }

    @Override
    public ItineraryResponse findById(Long id) {
        return itineraryMapper.toResponse(getById(id));
    }

    @Override
    public Page<ItineraryResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itineraryRepository.findAll(pageable).map(itineraryMapper::toResponse);
    }

    @Override
    public ItineraryResponse update(Long id, ItineraryRequest request) {
        Itinerary itinerary = getById(id);
        itineraryMapper.updateEntity(request, itinerary);
        if (request.tourId() != null) {
            itinerary.setTour(getTourById(request.tourId()));
        }
        return itineraryMapper.toResponse(itineraryRepository.save(itinerary));
    }

    @Override
    public void delete(Long id) {
        Itinerary itinerary = getById(id);
        itineraryRepository.delete(itinerary);
    }

    private Itinerary getById(Long id) {
        return itineraryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Itinerary not found with id: " + id));
    }

    private Tour getTourById(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tour not found with id: " + id));
    }
}
