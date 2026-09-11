package com.istad.tourmanagementapi.featurs.Activity;

import com.istad.tourmanagementapi.featurs.Activity.dto.ActivityRequest;
import com.istad.tourmanagementapi.featurs.Activity.dto.ActivityResponse;
import com.istad.tourmanagementapi.featurs.Activity.entity.Activity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
@Mapper(componentModel = "spring")
public interface ActivityMapper {

    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    Activity toEntity(ActivityRequest request);

    @Mapping(target = "tourId", source = "tour.id")
    ActivityResponse toResponse(Activity activity);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntity(
            ActivityRequest request,
            @MappingTarget Activity activity
    );
}
