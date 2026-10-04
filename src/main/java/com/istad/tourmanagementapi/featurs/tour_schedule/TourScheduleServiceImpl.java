package com.istad.tourmanagementapi.featurs.tour_schedule;
import com.istad.tourmanagementapi.featurs.booking.BookingRepository;
import com.istad.tourmanagementapi.featurs.enums.TourGuideStatus;
import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.tour.TourRepository;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import com.istad.tourmanagementapi.featurs.tour_guide.TourGuideRepository;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideResponse;
import com.istad.tourmanagementapi.featurs.tour_guide.entity.TourGuide;
import com.istad.tourmanagementapi.featurs.tour_guide.mapper.TourGuideMapper;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.CreateTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.PatchTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.TourScheduleResponse;
import com.istad.tourmanagementapi.featurs.tour_schedule.entity.TourSchedule;
import com.istad.tourmanagementapi.featurs.tour_schedule.mapper.TourScheduleMapper;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourScheduleServiceImpl implements TourScheduleService {

    private final TourScheduleRepository scheduleRepository;
    private final TourScheduleMapper scheduleMapper;
    private final TourRepository tourRepository;
    private final TourGuideRepository tourGuideRepository;
    private final TourGuideMapper tourGuideMapper;
    private final BookingRepository bookingRepository;


    // CREATE
    @Override
    public TourScheduleResponse create(
            CreateTourScheduleRequest request
    ) {

        Tour tour = getTourById(request.tourId());

        // Calculate the expected end date based on tour duration
        // startDate = 2026-09-20
        // durationDays = 3
        // expectedEndDate = duration-1 = 2026-09-22
        LocalDate expectedEndDate =
                request.startDate()
                        .plusDays(tour.getDurationDays() - 1);

        // Validate end date
        if (!request.endDate().equals(expectedEndDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date must be " + expectedEndDate
                            + " because this tour has "
                            + tour.getDurationDays()
                            + " day"
            );
        }

        TourSchedule schedule =
                scheduleMapper.toEntity(request);

        schedule.setTour(tour);
        schedule.setCapacity(tour.getMaxParticipants());

        schedule.setStatus(
                TourScheduleStatus.OPEN
        );

        schedule.setDeleted(false);

        TourSchedule savedSchedule =
                scheduleRepository.save(schedule);

        return toResponse(savedSchedule);
    }


    // FIND BY ID
    @Override
    public TourScheduleResponse findById(Long id) {

        TourSchedule schedule =
                scheduleRepository
                        .findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Schedule not found with id: " + id
                                )
                        );

        return toResponse(schedule);
    }


    // FIND ALL
    @Override
    public PageResponse<TourScheduleResponse> findAll(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<TourScheduleResponse> schedules =
                scheduleRepository
                        .findAllByIsDeletedFalse(pageable)
                        .map(this::toResponse);

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
            Long id,
            PatchTourScheduleRequest request
    ) {

        TourSchedule schedule = getById(id);

        // Validate final dates
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

        TourSchedule updatedSchedule =
                scheduleRepository.save(schedule);

        return toResponse(updatedSchedule);
    }


    // DELETE - SOFT DELETE
    @Override
    public void delete(Long id) {

        TourSchedule schedule =
                getById(id);

        schedule.setDeleted(true);

        scheduleRepository.save(schedule);
    }


    // GET BY ID
    private TourSchedule getById(Long id) {

        return scheduleRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Schedule not found with id: " + id
                        )
                );
    }


    // FIND BY STATUS
    @Override
    public PageResponse<TourScheduleResponse> findByStatus(
            TourScheduleStatus status,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<TourScheduleResponse> schedules =
                scheduleRepository
                        .findAllByStatusAndIsDeletedFalse(
                                status,
                                pageable
                        )
                        .map(this::toResponse);

        return PageResponse.<TourScheduleResponse>builder()
                .items(schedules.getContent())
                .size(schedules.getSize())
                .pageNumber(schedules.getNumber())
                .totalElements(schedules.getTotalElements())
                .totalPages(schedules.getTotalPages())
                .build();
    }


    // GET TOUR BY ID
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


    // ASSIGN GUIDE
    @Override
    public void assignGuide(
            Long scheduleId,
            Long guideId
    ) {

        TourSchedule schedule =
                getById(scheduleId);

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

        // Check if guide already assigned
        if (schedule.getGuides().contains(guide)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Guide is already assigned to this schedule"
            );
        }

        // Check schedule conflict
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

        schedule.getGuides().add(guide);

        scheduleRepository.save(schedule);
    }


    // UNASSIGN GUIDE
    @Override
    public void unassignGuide(
            Long scheduleId,
            Long guideId
    ) {

        TourSchedule schedule =
                getById(scheduleId);

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

        if (!schedule.getGuides().contains(guide)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Guide is not assigned to this schedule"
            );
        }

        schedule.getGuides().remove(guide);

        scheduleRepository.save(schedule);
    }


    // FIND BY TOUR ID
    @Override
    public PageResponse<TourScheduleResponse> findByTourId(
            Long tourId,
            int page,
            int size
    ) {

        // Check if tour exists
        getTourById(tourId);

        Pageable pageable =
                PageRequest.of(page, size);

        Page<TourScheduleResponse> schedules =
                scheduleRepository
                        .findAllByTourIdAndIsDeletedFalse(
                                tourId,
                                pageable
                        )
                        .map(this::toResponse);

        return PageResponse.<TourScheduleResponse>builder()
                .items(schedules.getContent())
                .size(schedules.getSize())
                .pageNumber(schedules.getNumber())
                .totalElements(schedules.getTotalElements())
                .totalPages(schedules.getTotalPages())
                .build();
    }


    // FIND GUIDES BY SCHEDULE ID
    @Override
    @Transactional
    public List<TourGuideResponse> findGuidesByScheduleId(
            Long scheduleId
    ) {

        getById(scheduleId);

        return scheduleRepository
                .findGuidesByScheduleId(scheduleId)
                .stream()
                .map(tourGuideMapper::toResponse)
                .toList();
    }



    private TourScheduleResponse toResponse(
            TourSchedule schedule
    ) {
        Integer bookedPeople =
                bookingRepository.countPaidPeopleByScheduleId(
                        schedule.getId()
                );

        Integer availableCapacity = Math.max(
                0,
                schedule.getCapacity() - bookedPeople
        );

        TourScheduleResponse response =
                scheduleMapper.toResponse(schedule);

        return new TourScheduleResponse(
                response.id(),
                response.tourId(),
                response.startDate(),
                response.endDate(),
                response.capacity(),
                availableCapacity,
                schedule.getStatus(),
                response.isDeleted()
        );
    }
}