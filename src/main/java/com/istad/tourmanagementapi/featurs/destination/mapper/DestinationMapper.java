package com.istad.tourmanagementapi.featurs.destination.mapper;

import com.istad.tourmanagementapi.featurs.destination.dto.CreateDestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.destination.dto.UpdateDestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.media.Media;
import org.mapstruct.*;

import java.util.List;
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface DestinationMapper {

    Destination toEntity(CreateDestinationRequest request);

    @Mapping(target = "imageIds", source = "media")
    DestinationResponse toResponse(Destination destination);

    default List<Integer> map(List<Media> media) {
        if (media == null) {
            return List.of();
        }

        return media.stream()
                .map(Media::getId)
                .toList();
    }

    void updateEntity(
            UpdateDestinationRequest request,
            @MappingTarget Destination destination
    );
}