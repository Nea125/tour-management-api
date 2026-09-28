package com.istad.tourmanagementapi.featurs.participant;

import com.istad.tourmanagementapi.featurs.participant.dto.ParticipantRequest;
import com.istad.tourmanagementapi.featurs.participant.dto.ParticipantResponse;
import org.springframework.data.domain.Page;

public interface ParticipantService {

    ParticipantResponse create(ParticipantRequest request);

    ParticipantResponse findById(Long id);

    Page<ParticipantResponse> findAll(int page, int size);

    ParticipantResponse update(Long id, ParticipantRequest request);

    void delete(Long id);
}
