package com.istad.tourmanagementapi.featurs.tour_schedule;

import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.CreateTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.PatchTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.TourScheduleResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;

public interface TourScheduleService {

    TourScheduleResponse create(CreateTourScheduleRequest request);

    TourScheduleResponse findById(String id);


    TourScheduleResponse update(String id, PatchTourScheduleRequest request);

    void delete(String id);


    PageResponse<TourScheduleResponse> findAll(
            int page,
            int size
    );

    PageResponse<TourScheduleResponse> findByStatus(
            TourScheduleStatus status,
            int page,
            int size
    );
    void assignGuide(String scheduleId, Long guideId);

    void unassignGuide(String scheduleId, Long guideId);
}
