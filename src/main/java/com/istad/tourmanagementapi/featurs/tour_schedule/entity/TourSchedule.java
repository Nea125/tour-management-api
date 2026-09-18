package com.istad.tourmanagementapi.featurs.tour_schedule.entity;

import com.istad.tourmanagementapi.featurs.booking.entity.Booking;

import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.tour_guide.entity.TourGuide;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tour_schedules")
public class TourSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer capacity;

    boolean isDeleted;

   // Relationship with Guide [Many tours have many guides have junction table called tour_schedule_guides]
    // One tour schedule can have many guides, and one guide can be assigned to many tour schedules.
    @ManyToMany
    @JoinTable(
            name = "tour_schedule_guides",
            joinColumns = @JoinColumn(name = "tour_schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_guide_id")
    )
    private List<TourGuide> guides;

   // Relationship with Tour [Many schedules have one tour]
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    // Relationship with Booking [One tours have many bookings]
    @OneToMany(mappedBy = "schedule")
    private List<Booking> bookings;

    TourScheduleStatus status;
}