package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    Booking toEntity(BookingRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "scheduleId", source = "schedule.id")
    BookingResponse toResponse(Booking booking);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    void updateEntity(
            BookingRequest request,
            @MappingTarget Booking booking
    );
}