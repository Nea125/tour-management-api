package com.istad.tourmanagementapi.featurs.tour_guide;

import com.istad.tourmanagementapi.featurs.profile.dto.UserProfileResponse;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.PatchTourGuideRequest;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideRequest;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideResponse;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.TourScheduleResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;

import java.util.List;

public interface TourGuideService {

    TourGuideResponse create(
            TourGuideRequest request
    );

    TourGuideResponse findById(
            Long id
    );

    PageResponse<TourGuideResponse> findAll(
            int page,
            int size
    );

    TourGuideResponse update(
            Long id,
            PatchTourGuideRequest request
    );

    void delete(
            Long id
    );

    UserProfileResponse findUserByGuideId(Long guideId);

    List<TourScheduleResponse> findSchedulesByGuideId(Long guideId);
}