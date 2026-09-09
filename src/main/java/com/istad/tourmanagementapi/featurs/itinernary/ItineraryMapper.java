package com.istad.tourmanagementapi.featurs.itinernary;

import com.istad.tourmanagementapi.featurs.itinernary.dto.ItineraryRequest;
import com.istad.tourmanagementapi.featurs.itinernary.dto.ItineraryResponse;
import com.istad.tourmanagementapi.featurs.itinernary.entity.Itinerary;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ItineraryMapper {

    @Mapping(target = "tour", ignore = true)
    Itinerary toEntity(ItineraryRequest request);

    @Mapping(target = "tourId", source = "tour.id")
    ItineraryResponse toResponse(Itinerary itinerary);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tour", ignore = true)
    void updateEntity(ItineraryRequest request, @MappingTarget Itinerary itinerary);
}
