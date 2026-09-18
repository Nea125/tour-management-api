package com.istad.tourmanagementapi.featurs.tour_schedule;


import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.tour_schedule.dto.TourScheduleResponse;
import com.istad.tourmanagementapi.featurs.tour_schedule.entity.TourSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TourScheduleRepository
        extends JpaRepository<TourSchedule, String> {

    Optional<TourSchedule> findByIdAndIsDeletedFalse(String id);

    Page<TourSchedule> findAllByIsDeletedFalse(Pageable pageable);



    Page<TourSchedule> findAllByStatusAndIsDeletedFalse(
            TourScheduleStatus status,
            Pageable pageable
    );

// Check if the guide has a schedule conflict
    @Query("""
    SELECT COUNT(s) > 0
    FROM TourSchedule s
    JOIN s.guides g
    WHERE g.id = :guideId
      AND s.isDeleted = false
      AND s.id <> :scheduleId
      AND s.startDate <= :endDate
      AND s.endDate >= :startDate
""")
    boolean existsGuideScheduleConflict(
            @Param("guideId") Long guideId,
            @Param("scheduleId") String scheduleId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}