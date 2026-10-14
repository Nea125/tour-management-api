package com.istad.tourmanagementapi.featurs.tour.entity;

import com.istad.tourmanagementapi.featurs.media.Media;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// Same as DestinationMedia: keeps tour images in sortOrder
// so an updated image stays in the same position

@Entity
@Table(name = "tour_medias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
