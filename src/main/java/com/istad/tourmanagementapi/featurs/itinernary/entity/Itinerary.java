package com.istad.tourmanagementapi.featurs.itinernary.entity;

import com.istad.tourmanagementapi.featurs.guide.entity.Guide;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "itineraries")
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




    @Positive
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // Relationships with tour [Many-to-One: Many Itineraries have One Tour]
    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;
    // Relationships with itineraryActivity [One-to-Many: One Itinerary has Many ItineraryActivity]

    @OneToMany(mappedBy = "itinerary")
    private List<ItineraryActivity> activities;
}