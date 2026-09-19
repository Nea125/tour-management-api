package com.istad.tourmanagementapi.featurs.profile.mapper;
import com.istad.tourmanagementapi.featurs.media.mapper.MediaMapper;
import com.istad.tourmanagementapi.featurs.profile.dto.PatchUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.UserProfileResponse;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;




@Mapper(
        componentModel = "spring",
        uses = MediaMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserProfileMapper {
//    source = where the data comes from [Parameter]
//    target = where the data goes to [Return type]
//    Take profileImage from UserProfile and put it into profileImage of UserProfileResponse
    @Mapping(
            target = "profileImage",
            source = "profileImage"
    )
    UserProfileResponse toResponse(
            UserProfile userProfile
    );

    @Mapping(
            target = "profileImage",
            ignore = true
    )
// Ignore profileImage because it is updated separately
// PatchUserProfileRequest -> [Source]
// UserProfile -> [Target]
    @Mapping(
            target = "profileImage",
            ignore = true
    )
    void updateEntity(
            PatchUserProfileRequest request,
            @MappingTarget UserProfile userProfile
    );
}