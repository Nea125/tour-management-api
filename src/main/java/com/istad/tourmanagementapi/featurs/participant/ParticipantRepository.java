package com.istad.tourmanagementapi.featurs.participant;

import com.istad.tourmanagementapi.featurs.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
