package com.istad.tourmanagementapi.featurs.media;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medias")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 10)
    private String extension;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false, length = 32)
    private String mediaType;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}