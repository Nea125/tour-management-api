package com.istad.tourmanagementapi.featurs.tour.entity;

import com.istad.tourmanagementapi.featurs.destination.entity.Destination;
import com.istad.tourmanagementapi.featurs.Activity.entity.Activity;
import com.istad.tourmanagementapi.featurs.review.entity.Review;
import com.istad.tourmanagementapi.featurs.schedule.entity.Schedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tours")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;


    @Positive
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;


    @Column(name = "duration_nights", nullable = false)
    private Integer durationNights;


    @Positive
    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url")
    private List<String> images;

    @Column(name = "is_deleted", nullable = false)
    Boolean isDeleted;

    //Relationship with Destination [Many tours have one destination]
    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    // Relationship with Schedule [One tours have many schedules]
    @OneToMany(mappedBy = "tour")
    List<Schedule> schedules;

    // Relationship with Activity [One tours have Many Activities]
    @OneToMany(mappedBy = "tour")
    private List<Activity> activities;

    // Relationship with Review [One tours have Many reviews]
    @OneToMany(mappedBy = "tour")
    private List<Review> reviews;
}