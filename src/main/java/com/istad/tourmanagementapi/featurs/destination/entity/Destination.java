package com.istad.tourmanagementapi.featurs.destination.entity;
import com.istad.tourmanagementapi.featurs.enums.DestinationStatus;
import jakarta.persistence.*;
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
@Table(name = "destinations")
public class Destination  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String province;

    private String country;

    private Double latitude;

    private Double longitude;

    @Column(name = "image_url")
    private List<String> imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DestinationStatus status;
}