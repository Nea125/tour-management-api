package com.istad.tourmanagementapi.featurs.guide;

import com.istad.tourmanagementapi.featurs.guide.dto.GuideRequest;
import com.istad.tourmanagementapi.featurs.guide.dto.GuideResponse;
import com.istad.tourmanagementapi.featurs.guide.entity.Guide;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GuideMapper {

    @Mapping(target = "user", ignore = true)
    Guide toEntity(GuideRequest request);

    @Mapping(target = "userId", source = "user.id")
    GuideResponse toResponse(Guide guide);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    void updateEntity(GuideRequest request, @MappingTarget Guide guide);
}
