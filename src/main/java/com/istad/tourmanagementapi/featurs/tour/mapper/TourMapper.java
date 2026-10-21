package com.istad.tourmanagementapi.featurs.tour.mapper;

import com.istad.tourmanagementapi.featurs.tour.dto.CreateTourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.TourImageResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.UpdateTourRequest;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import com.istad.tourmanagementapi.featurs.tour.entity.TourMedia;
import org.mapstruct.*;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TourMapper {

    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "images", ignore = true)
    Tour toEntity(CreateTourRequest request);

    @Mapping(target = "destinationId", source = "destination.id")
    @Mapping(target = "images", source = "images")
    TourResponse toResponse(Tour tour);

    default List<TourImageResponse> toImageResponses(List<TourMedia> images) {

        if (images == null) {
            return List.of();
        }

        return images.stream()
                .sorted(Comparator.comparing(TourMedia::getSortOrder))
                .map(this::toImageResponse)
                .toList();
    }

    default TourImageResponse toImageResponse(TourMedia tm) {
        return new TourImageResponse(
                tm.getMedia().getId(),
                tm.getSortOrder()
        );
    }

    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateEntity(
            UpdateTourRequest request,
            @MappingTarget Tour tour
    );
}
