package com.istad.tourmanagementapi.featurs.booking;

import com.istad.tourmanagementapi.featurs.booking.entity.BookingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingParticipantRepository extends JpaRepository<BookingParticipant, Long> {
}
