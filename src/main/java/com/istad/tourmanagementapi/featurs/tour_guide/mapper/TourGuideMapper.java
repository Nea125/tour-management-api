package com.istad.tourmanagementapi.featurs.tour_guide.mapper;

import com.istad.tourmanagementapi.featurs.tour_guide.dto.PatchTourGuideRequest;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideRequest;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideResponse;
import com.istad.tourmanagementapi.featurs.tour_guide.entity.TourGuide;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TourGuideMapper {

    // CREATE
    @Mapping(target = "user", ignore = true)
    TourGuide toEntity(TourGuideRequest request);

    // RESPONSE
    @Mapping(target = "userId", source = "user.id")
    TourGuideResponse toResponse(TourGuide guide);

    // PATCH
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "user", ignore = true)
    void updateEntity(
            PatchTourGuideRequest request,
            @MappingTarget TourGuide guide
    );
}