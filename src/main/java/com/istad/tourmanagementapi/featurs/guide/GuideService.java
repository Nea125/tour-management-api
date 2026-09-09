package com.istad.tourmanagementapi.featurs.guide;

import com.istad.tourmanagementapi.featurs.guide.dto.GuideRequest;
import com.istad.tourmanagementapi.featurs.guide.dto.GuideResponse;
import org.springframework.data.domain.Page;

public interface GuideService {

    GuideResponse create(GuideRequest request);

    GuideResponse findById(Long id);

    Page<GuideResponse> findAll(int page, int size);

    GuideResponse update(Long id, GuideRequest request);

    void delete(Long id);
}
