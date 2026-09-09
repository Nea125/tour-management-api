package com.istad.tourmanagementapi.featurs.user;

import com.istad.tourmanagementapi.featurs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
