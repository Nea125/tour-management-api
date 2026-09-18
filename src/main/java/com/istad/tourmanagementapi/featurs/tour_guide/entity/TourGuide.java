package com.istad.tourmanagementapi.featurs.tour_guide.entity;

import com.istad.tourmanagementapi.featurs.enums.TourGuideStatus;
import com.istad.tourmanagementapi.featurs.tour_schedule.entity.TourSchedule;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tour_guides")
public class TourGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @PositiveOrZero
    @Column(name = "experience_years")
    private Integer experienceYears;

    private List<String> languages;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TourGuideStatus status;
    // Relationship with Schedule [Many tours have many guides have junction table called tour_schedule_guides]
    @ManyToMany(mappedBy = "guides")
    private List<TourSchedule> schedules;

    // Relationship with User [One guide has one user]
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;


}