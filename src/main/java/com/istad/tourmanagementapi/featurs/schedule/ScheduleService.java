package com.istad.tourmanagementapi.featurs.schedule;

import com.istad.tourmanagementapi.featurs.schedule.dto.ScheduleRequest;
import com.istad.tourmanagementapi.featurs.schedule.dto.ScheduleResponse;
import org.springframework.data.domain.Page;

public interface ScheduleService {

    ScheduleResponse create(ScheduleRequest request);

    ScheduleResponse findById(Long id);

    Page<ScheduleResponse> findAll(int page, int size);

    ScheduleResponse update(Long id, ScheduleRequest request);

    void delete(Long id);
}
