package com.istad.tourmanagementapi.featurs.schedule;

import com.istad.tourmanagementapi.featurs.schedule.dto.ScheduleRequest;
import com.istad.tourmanagementapi.featurs.schedule.dto.ScheduleResponse;
import com.istad.tourmanagementapi.featurs.schedule.entity.Schedule;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "tour", ignore = true)
    Schedule toEntity(ScheduleRequest request);

    @Mapping(target = "tourId", source = "tour.id")
    ScheduleResponse toResponse(Schedule schedule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tour", ignore = true)
    void updateEntity(ScheduleRequest request, @MappingTarget Schedule schedule);
}
