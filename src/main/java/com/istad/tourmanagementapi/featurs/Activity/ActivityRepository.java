package com.istad.tourmanagementapi.featurs.Activity;

import com.istad.tourmanagementapi.featurs.Activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findByIsDeletedFalse(Pageable pageable);

    Optional<Activity> findByIdAndIsDeletedFalse(Long id);
}
