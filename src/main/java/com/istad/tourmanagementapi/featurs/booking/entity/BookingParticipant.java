package com.istad.tourmanagementapi.featurs.booking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participants")
public class BookingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Size(max = 150)
    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String gender;

    @Past
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "phone must be a valid number")
    private String phone;

    @Email
    private String email;

 // Relationships with booking [Many-to-One: Many Participants have One Booking]
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}