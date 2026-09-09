package com.istad.tourmanagementapi.featurs.user;

import com.istad.tourmanagementapi.featurs.user.dto.UserRequest;
import com.istad.tourmanagementapi.featurs.user.dto.UserResponse;
import com.istad.tourmanagementapi.featurs.user.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UserRequest request, @MappingTarget User user);
}
