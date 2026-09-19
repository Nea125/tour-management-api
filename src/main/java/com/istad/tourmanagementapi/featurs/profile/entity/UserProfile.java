package com.istad.tourmanagementapi.featurs.profile.entity;

import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.tour_guide.entity.TourGuide;
import com.istad.tourmanagementapi.featurs.review.entity.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
   //keycloak user ID
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;


    @Column(name = "last_name", nullable = false)
    private String lastName;


    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "phone must be a valid number")
    private String phone;

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private Media profileImage;

    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    boolean isDeleted;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)


    private UserStatus status;

    //Relationship with guide [One user can have one guide or null]
    @OneToOne
    TourGuide guide;

    // Relationship with Booking [One user can have many bookings]
    @OneToMany(mappedBy = "user")
    List<Booking> bookings;

// Relationship with Review [One user can have many reviews]
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}