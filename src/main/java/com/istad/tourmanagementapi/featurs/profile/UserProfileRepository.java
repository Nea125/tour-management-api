package com.istad.tourmanagementapi.featurs.profile;

import com.istad.tourmanagementapi.featurs.enums.UserRole;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, String> {

    boolean existsByEmail(String email);

    Optional<UserProfile> findByIdAndIsDeletedFalse(
            String id
    );

    Page<UserProfile> findAllByIsDeletedFalse(
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(u)
    FROM UserProfile u
    WHERE u.role = :role
""")
    Long countUsersByRole(
            @Param("role") UserRole role
    );
}