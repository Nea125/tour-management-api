package com.istad.tourmanagementapi.featurs.profile;

import com.istad.tourmanagementapi.featurs.enums.UserRole;
import com.istad.tourmanagementapi.featurs.enums.UserStatus;
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

    // COUNT USERS BY ROLE AND STATUS
    @Query("""
    SELECT COUNT(user)
    FROM UserProfile user
    WHERE user.role = :role
      AND user.status = :status
      AND user.isDeleted = false
""")
    Long countUsersByRoleAndStatus(
            @Param("role") UserRole role,
            @Param("status") UserStatus status
    );
}