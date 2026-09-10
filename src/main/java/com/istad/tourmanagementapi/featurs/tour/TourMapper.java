package com.istad.tourmanagementapi.featurs.tour;

import com.istad.tourmanagementapi.featurs.tour.dto.TourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TourMapper {

    @Mapping(target = "destination", ignore = true)
    Tour toEntity(TourRequest request);

    @Mapping(target = "destinationId", source = "destination.id")
    TourResponse toResponse(Tour tour);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "destination", ignore = true)
    void updateEntity(
            TourRequest request,
            @MappingTarget Tour tour
    );
}
