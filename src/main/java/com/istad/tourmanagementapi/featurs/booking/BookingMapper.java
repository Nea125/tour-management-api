package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tourSchedule", ignore = true)
    Booking toEntity(BookingRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "scheduleId", source = "tourSchedule.id")
    BookingResponse toResponse(Booking booking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tourSchedule", ignore = true)
    void updateEntity(BookingRequest request, @MappingTarget Booking booking);
}
