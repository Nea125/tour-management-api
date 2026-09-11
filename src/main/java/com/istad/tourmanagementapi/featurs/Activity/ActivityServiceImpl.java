package com.istad.tourmanagementapi.featurs.Activity;

import com.istad.tourmanagementapi.featurs.Activity.dto.ActivityRequest;
import com.istad.tourmanagementapi.featurs.Activity.dto.ActivityResponse;
import com.istad.tourmanagementapi.featurs.Activity.entity.Activity;
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
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final TourRepository tourRepository;
    private final ActivityMapper activityMapper;

    @Override
    public ActivityResponse create(ActivityRequest request) {

        Activity activity = activityMapper.toEntity(request);

        activity.setTour(getTourById(request.tourId()));

        return activityMapper.toResponse(
                activityRepository.save(activity)
        );
    }

    @Override
    public ActivityResponse findById(Long id) {
        return activityMapper.toResponse(getById(id));
    }

    @Override
    public Page<ActivityResponse> findAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return activityRepository
                .findByIsDeletedFalse(pageable)
                .map(activityMapper::toResponse);
    }

    @Override
    public ActivityResponse update(
            Long id,
            ActivityRequest request
    ) {

        Activity activity = getById(id);

        activityMapper.updateEntity(request, activity);

        if (request.tourId() != null) {
            activity.setTour(
                    getTourById(request.tourId())
            );
        }

        return activityMapper.toResponse(
                activityRepository.save(activity)
        );
    }

    @Override
    public void delete(Long id) {

        Activity activity = getById(id);

        activity.setDeleted(true);

        activityRepository.save(activity);
    }

    private Activity getById(Long id) {

        return activityRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Activity not found with id: " + id
                ));
    }

    private Tour getTourById(Long id) {

        return tourRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tour not found with id: " + id
                ));
    }
}
