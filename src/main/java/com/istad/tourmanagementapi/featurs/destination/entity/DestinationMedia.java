package com.istad.tourmanagementapi.featurs.destination.entity;

import com.istad.tourmanagementapi.featurs.media.Media;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// Create this entity because want to make display image in sortOrder
// to make properly position when update

@Entity
@Table(name = "destination_medias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}