package com.istad.tourmanagementapi.featurs.destination.entity;


import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.tour.entity.Tour;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(max = 150)
    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    String description;



    @Size(max = 100)
    private String province;


    @Size(max = 100)
    @Column(nullable = false)
    private String country;

    @ManyToMany
    @JoinTable(
            name = "destination_medias",
            joinColumns = @JoinColumn(name = "destination_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private List<Media> media;
//    private List<Media> media = new ArrayList<>();
    @NotNull
    Long latitude;


    @OneToMany(mappedBy = "destination")
    private List<Tour> tours;



    @NotNull(message = "Longitude is required")
    Double longitude;
    boolean isDeleted;
}