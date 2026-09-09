package com.istad.tourmanagementapi.featurs.guide.entity;

import com.istad.tourmanagementapi.featurs.enums.GuideStatus;
import com.istad.tourmanagementapi.featurs.schedule.entity.Schedule;
import com.istad.tourmanagementapi.featurs.user.entity.User;
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
@Table(name = "guides")
public class Guide {

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
    private GuideStatus status;
    // Relationship with Schedule [Many tours have many guides have junction table called tour_schedule_guides]
    @ManyToMany(mappedBy = "guides")
    private List<Schedule> schedules;

    // Relationship with User [One guide has one user]
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}