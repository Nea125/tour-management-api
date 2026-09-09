package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.dto.BookingParticipantRequest;
import com.istad.tourmanagementapi.featurs.booking.dto.BookingParticipantResponse;
import com.istad.tourmanagementapi.featurs.booking.entity.BookingParticipant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BookingParticipantMapper {

    @Mapping(target = "booking", ignore = true)
    BookingParticipant toEntity(BookingParticipantRequest request);

    @Mapping(target = "bookingId", source = "booking.id")
    BookingParticipantResponse toResponse(BookingParticipant participant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "booking", ignore = true)
    void updateEntity(BookingParticipantRequest request, @MappingTarget BookingParticipant participant);
}
