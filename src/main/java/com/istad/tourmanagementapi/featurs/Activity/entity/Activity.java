package com.istad.tourmanagementapi.featurs.Activity.entity;
import jakarta.persistence.Entity;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "activities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;
// Creating a collection table for activity timelines
// Each activity can have multiple timelines
//Just separate the timelines from the activity
    @ElementCollection
    @CollectionTable(
            name = "activity_timelines",
            joinColumns = @JoinColumn(name = "activity_id")
    )
    private List<ActivityTimeline> timeline;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    private boolean isDeleted;
}