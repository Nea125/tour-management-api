package com.istad.tourmanagementapi.featurs.tour_schedule;

import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideResponse;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.CreateTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.PatchTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.TourScheduleResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;

import java.util.List;

public interface TourScheduleService {

    TourScheduleResponse create(CreateTourScheduleRequest request);

    TourScheduleResponse findById(Long id);


    TourScheduleResponse update(Long id, PatchTourScheduleRequest request);

    void delete(Long id);


    PageResponse<TourScheduleResponse> findAll(
            int page,
            int size
    );

    PageResponse<TourScheduleResponse> findByStatus(
            TourScheduleStatus status,
            int page,
            int size
    );
    void assignGuide(Long scheduleId, Long guideId);

    void unassignGuide(Long scheduleId, Long guideId);
    PageResponse<TourScheduleResponse> findByTourId(
            Long tourId,
            int page,
            int size
    );
    List<TourGuideResponse> findGuidesByScheduleId(Long scheduleId);
}
