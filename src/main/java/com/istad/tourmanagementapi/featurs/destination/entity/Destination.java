package com.istad.tourmanagementapi.featurs.destination.entity;

import com.istad.tourmanagementapi.featurs.enums.DestinationStatus;
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

    @Column(nullable = false,name = "image_url")
    List<String> imageUrl;

    @NotNull
    Long latitude;

    @NotNull
    Long longitude;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DestinationStatus status;
}