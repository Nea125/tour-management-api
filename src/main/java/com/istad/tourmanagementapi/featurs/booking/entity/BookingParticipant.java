package com.istad.tourmanagementapi.featurs.booking.entity;

import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// No createdAt/updatedAt in the schema, so this does NOT extend BaseEntity.
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participants")
public class BookingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String phone;

    private String email;
}