package com.istad.tourmanagementapi.featurs.booking.entity;

import com.istad.tourmanagementapi.featurs.enums.BookingStatus;
import com.istad.tourmanagementapi.featurs.schedule.entity.Schedule;
import com.istad.tourmanagementapi.featurs.user.entity.User;
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
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name = "booking_code", nullable = false, unique = true)
    private String bookingCode;



    @NotNull
    @Positive
    @Column(name = "number_of_people", nullable = false)
    private Integer numberOfPeople;

    @NotNull
    @PastOrPresent
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "special_request", columnDefinition = "TEXT")
    private String specialRequest;


 // Relationships with user [Many to one: One booking has one user]
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relationships with tourSchedule [Many to one: One booking has one tourSchedule]
    @ManyToOne
    @JoinColumn(name = "tour_schedule_id", nullable = false)
    private Schedule schedule;
}