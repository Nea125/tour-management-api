package com.istad.tourmanagementapi.featurs.participant.mapper;

import com.istad.tourmanagementapi.featurs.participant.dto.ParticipantRequest;
import com.istad.tourmanagementapi.featurs.participant.dto.ParticipantResponse;
import com.istad.tourmanagementapi.featurs.participant.entity.Participant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    @Mapping(target = "booking", ignore = true)
    Participant toEntity(ParticipantRequest request);

    @Mapping(target = "bookingId", source = "booking.id")
    ParticipantResponse toResponse(Participant participant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "booking", ignore = true)
    void updateEntity(ParticipantRequest request, @MappingTarget Participant participant);
}
