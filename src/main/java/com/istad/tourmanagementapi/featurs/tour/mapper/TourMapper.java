package com.istad.tourmanagementapi.featurs.tour.mapper;

import com.istad.tourmanagementapi.featurs.destination.dto.CreateDestinationRequest;
import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.tour.dto.CreateTourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.UpdateTourRequest;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface TourMapper {

    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "media", ignore = true)
    Tour toEntity(CreateTourRequest request);

    @Mapping(target = "destinationId", source = "destination.id")
    @Mapping(target = "imageIds", source = "media")
    TourResponse toResponse(Tour tour);

    default List<Integer> map(List<Media> media) {

        if (media == null) {
            return List.of();
        }

        return media.stream()
                .map(Media::getId)
                .toList();
    }

    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "media", ignore = true)
    void updateEntity(
            UpdateTourRequest request,
            @MappingTarget Tour tour
    );
}