package com.istad.tourmanagementapi.featurs.tour_schedule;
import com.istad.tourmanagementapi.featurs.enums.TourGuideStatus;
import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.tour_guide.TourGuideRepository;
import com.istad.tourmanagementapi.featurs.tour_guide.entity.TourGuide;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.CreateTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.PatchTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.TourScheduleResponse;
import com.istad.tourmanagementapi.featurs.tour_schedule.entity.TourSchedule;
import com.istad.tourmanagementapi.featurs.tour.TourRepository;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import com.istad.tourmanagementapi.featurs.tour_schedule.mapper.TourScheduleMapper;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TourScheduleServiceImpl implements TourScheduleService {

    private final TourScheduleRepository scheduleRepository;
    private final TourScheduleMapper scheduleMapper;
    private final TourRepository tourRepository;
    private final TourGuideRepository tourGuideRepository;


    // CREATE
    @Override
    public TourScheduleResponse create(
            CreateTourScheduleRequest request
    ) {

        // Allow end date equal or greater than start date
        if (request.startDate().isAfter(request.endDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date must be before or equal to end date"
            );
        }

        Tour tour = getTourById(request.tourId());

        TourSchedule schedule =
                scheduleMapper.toEntity(request);

        schedule.setTour(tour);

        schedule.setStatus(
                TourScheduleStatus.OPEN
        );

        schedule.setDeleted(false);

        return scheduleMapper.toResponse(
                scheduleRepository.save(schedule)
        );
    }


    // FIND BY ID
    // Only return non-deleted schedule
    @Override
    public TourScheduleResponse findById(String id) {

        TourSchedule schedule =
                scheduleRepository
                        .findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Schedule not found with id: " + id
                                )
                        );

        return scheduleMapper.toResponse(schedule);
    }


    // FIND ALL
    // Only return non-deleted schedules
    @Override

    public PageResponse<TourScheduleResponse> findAll(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<TourScheduleResponse> schedules =
                scheduleRepository
                        .findAllByIsDeletedFalse(pageable)
                        .map(scheduleMapper::toResponse);

        return PageResponse.<TourScheduleResponse>builder()
                .items(schedules.getContent())
                .size(schedules.getSize())
                .pageNumber(schedules.getNumber())
                .totalElements(schedules.getTotalElements())
                .totalPages(schedules.getTotalPages())
                .build();
    }


    // UPDATE

    @Override
    public TourScheduleResponse update(
            String id,
            PatchTourScheduleRequest request
    ) {

        TourSchedule schedule = getById(id);

        // Validate the final dates
        LocalDate startDate =
                request.startDate() != null
                        ? request.startDate()
                        : schedule.getStartDate();

        LocalDate endDate =
                request.endDate() != null
                        ? request.endDate()
                        : schedule.getEndDate();

        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date must not be after end date"
            );
        }

        // Update only non-null fields
        scheduleMapper.updateEntity(
                request,
                schedule
        );

        return scheduleMapper.toResponse(
                scheduleRepository.save(schedule)
        );
    }


    // DELETE - SOFT DELETE
    @Override
    public void delete(String id) {

        TourSchedule schedule =
                getById(id);

        schedule.setDeleted(true);

        scheduleRepository.save(schedule);
    }


    // Get schedule by ID
    // Used by UPDATE and DELETE
    private TourSchedule getById(String id) {

        return scheduleRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Schedule not found with id: " + id
                        )
                );
    }
    @Override
    public PageResponse<TourScheduleResponse> findByStatus(
            TourScheduleStatus status,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<TourScheduleResponse> schedules =
                scheduleRepository
                        .findAllByStatusAndIsDeletedFalse(
                                status,
                                pageable
                        )
                        .map(scheduleMapper::toResponse);

        return PageResponse.<TourScheduleResponse>builder()
                .items(schedules.getContent())
                .size(schedules.getSize())
                .pageNumber(schedules.getNumber())
                .totalElements(schedules.getTotalElements())
                .totalPages(schedules.getTotalPages())
                .build();
    }

    // Get tour by ID
    private Tour getTourById(Long id) {

        return tourRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Tour not found with id: " + id
                        )
                );
    }

    @Override
    public void assignGuide(
            String scheduleId,
            Long guideId
    ) {

        // 1. Find schedule
        TourSchedule schedule = getById(scheduleId);

        // 2. Find active guide
        TourGuide guide =
                tourGuideRepository
                        .findByIdAndStatus(
                                guideId,
                                TourGuideStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Active tour guide not found with id: "
                                                + guideId
                                )
                        );

        // 3. Check if guide is already assigned
        if (schedule.getGuides().contains(guide)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Guide is already assigned to this schedule"
            );
        }

        // 4. Check date conflict
        boolean conflict =
                scheduleRepository.existsGuideScheduleConflict(
                        guideId,
                        scheduleId,
                        schedule.getStartDate(),
                        schedule.getEndDate()
                );

        if (conflict) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Guide is already assigned to another schedule during this date range"
            );
        }

        // 5. Assign guide
        schedule.getGuides().add(guide);

        scheduleRepository.save(schedule);
    }

    @Override
    public void unassignGuide(
            String scheduleId,
            Long guideId
    ) {

        // 1. Find schedule
        TourSchedule schedule = getById(scheduleId);

        // 2. Find guide
        TourGuide guide =
                tourGuideRepository
                        .findById(guideId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Tour guide not found with id: "
                                                + guideId
                                )
                        );

        // 3. Check if guide is assigned
        if (!schedule.getGuides().contains(guide)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Guide is not assigned to this schedule"
            );
        }

        // 4. Remove guide
        schedule.getGuides().remove(guide);

        scheduleRepository.save(schedule);
    }
}