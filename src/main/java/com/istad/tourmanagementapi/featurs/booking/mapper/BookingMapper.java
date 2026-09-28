package com.istad.tourmanagementapi.featurs.booking.mapper;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingResponse;
import com.istad.tourmanagementapi.featurs.booking.dto.UpdateBookingRequest;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(
            target = "userId",
            source = "user.id"
    )
    @Mapping(
            target = "scheduleId",
            source = "schedule.id"
    )

    BookingResponse toResponse(Booking booking);

    void updateEntity(
            UpdateBookingRequest request,
            @MappingTarget Booking booking
    );
}