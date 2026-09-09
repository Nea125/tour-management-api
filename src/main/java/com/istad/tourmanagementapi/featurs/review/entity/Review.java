package com.istad.tourmanagementapi.featurs.review.entity;

import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import com.istad.tourmanagementapi.featurs.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;


// Relationship with Tour
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

// Relationship with Booking
    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // Relationship with User [One user can have many reviews]
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}