package com.istad.tourmanagementapi.featurs.tour_schedule.mapper;

import com.istad.tourmanagementapi.featurs.tour_schedule.dto.CreateTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.PatchTourScheduleRequest;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.TourScheduleResponse;
import com.istad.tourmanagementapi.featurs.tour_schedule.entity.TourSchedule;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TourScheduleMapper {

    // Create
    @Mapping(target = "tour", ignore = true)
    TourSchedule toEntity(CreateTourScheduleRequest request);

    // Response
    @Mapping(target = "tourId", source = "tour.id")
    TourScheduleResponse toResponse(TourSchedule schedule);

    // PATCH
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "tour", ignore = true)
    void updateEntity(
            PatchTourScheduleRequest request,
            @MappingTarget TourSchedule schedule
    );
}