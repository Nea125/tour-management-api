package com.istad.tourmanagementapi.featurs.destination.mapper;

import com.istad.tourmanagementapi.featurs.destination.dto.CreateDestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationImageResponse;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.destination.dto.UpdateDestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.destination.entity.DestinationMedia;
import com.istad.tourmanagementapi.featurs.media.Media;
import org.mapstruct.*;

import java.util.Comparator;
import java.util.List;
//@Mapper(
//        componentModel = "spring",
//        unmappedTargetPolicy = ReportingPolicy.IGNORE,
//        nullValuePropertyMappingStrategy =
//                NullValuePropertyMappingStrategy.IGNORE
//)
//public interface DestinationMapper {
//
//    Destination toEntity(CreateDestinationRequest request);
//
//    @Mapping(target = "imageIds", source = "media")
//    DestinationResponse toResponse(Destination destination);
//
//    default List<Integer> map(List<Media> media) {
//        if (media == null) {
//            return List.of();
//        }
//
//        return media.stream()
//                .map(Media::getId)
//                .toList();
//    }
//
//    void updateEntity(
//            UpdateDestinationRequest request,
//            @MappingTarget Destination destination
//    );
//}


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DestinationMapper {

    @Mapping(target = "images", source = "images")
    @Mapping(target = "isDeleted", source = "deleted")
    DestinationResponse toResponse(Destination destination);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "tours", ignore = true)
    Destination toEntity(CreateDestinationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "tours", ignore = true)
    void updateEntity(
            UpdateDestinationRequest request,
            @MappingTarget Destination destination
    );

    default List<DestinationImageResponse> toImageResponses(
            List<DestinationMedia> images
    ) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
                .sorted(Comparator.comparing(DestinationMedia::getSortOrder))
                .map(this::toImageResponse)
                .toList();
    }

    default DestinationImageResponse toImageResponse(DestinationMedia dm) {
        return new DestinationImageResponse(
                dm.getMedia().getId(),
                dm.getSortOrder()
        );
    }
}