package com.istad.tourmanagementapi.featurs.Activity;

import com.istad.tourmanagementapi.featurs.Activity.dto.ActivityRequest;
import com.istad.tourmanagementapi.featurs.Activity.dto.ActivityResponse;
import org.springframework.data.domain.Page;

public interface ActivityService {

    ActivityResponse create(ActivityRequest request);

    ActivityResponse findById(Long id);

    Page<ActivityResponse> findAll(int page, int size);

    ActivityResponse update(Long id, ActivityRequest request);

    void delete(Long id);
}
