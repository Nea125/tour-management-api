package com.istad.tourmanagementapi.featurs.profile;

import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, String> {

    boolean existsByEmail(String email);

    Optional<UserProfile> findByIdAndStatus(
            String id,
            UserStatus status
    );

    Page<UserProfile> findAllByStatus(
            UserStatus status,
            Pageable pageable
    );
}