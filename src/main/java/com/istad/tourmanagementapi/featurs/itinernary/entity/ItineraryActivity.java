package com.istad.tourmanagementapi.featurs.itinernary.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "itinerary_activities")
public class ItineraryActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @NotBlank
    @Size(max = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalTime startTime;

    private LocalTime endTime;

    @Size(max = 200)
    private String location;

    @PositiveOrZero
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    // Relationships with itinerary [Many-to-One: Many Activities have One Itinerary]
    @ManyToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
}