package com.istad.tourmanagementapi.featurs.schedule;

import com.istad.tourmanagementapi.featurs.schedule.dto.ScheduleRequest;
import com.istad.tourmanagementapi.featurs.schedule.dto.ScheduleResponse;
import com.istad.tourmanagementapi.featurs.schedule.entity.Schedule;
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
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final TourRepository tourRepository;

    @Override
    public ScheduleResponse create(ScheduleRequest request) {
        Schedule schedule = scheduleMapper.toEntity(request);
        schedule.setTour(getTourById(request.tourId()));
        return scheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleResponse findById(Long id) {
        return scheduleMapper.toResponse(getById(id));
    }

    @Override
    public Page<ScheduleResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return scheduleRepository.findAll(pageable).map(scheduleMapper::toResponse);
    }

    @Override
    public ScheduleResponse update(Long id, ScheduleRequest request) {
        Schedule schedule = getById(id);
        scheduleMapper.updateEntity(request, schedule);
        if (request.tourId() != null) {
            schedule.setTour(getTourById(request.tourId()));
        }
        return scheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Override
    public void delete(Long id) {
        Schedule schedule = getById(id);
        scheduleRepository.delete(schedule);
    }

    private Schedule getById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Schedule not found with id: " + id));
    }

    private Tour getTourById(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tour not found with id: " + id));
    }
}
