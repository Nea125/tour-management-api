package com.istad.tourmanagementapi.featurs.activity;

import com.istad.tourmanagementapi.featurs.activity.dto.ActivityRequest;
import com.istad.tourmanagementapi.featurs.activity.dto.ActivityResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;

public interface ActivityService {

    ActivityResponse create(ActivityRequest request);

    ActivityResponse findById(Long id);

    PageResponse<ActivityResponse> findAll(int page, int size);

    ActivityResponse update(Long id, ActivityRequest request);

    void delete(Long id);
}