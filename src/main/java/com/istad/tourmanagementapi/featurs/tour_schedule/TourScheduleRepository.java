package com.istad.tourmanagementapi.featurs.tour_schedule;
import com.istad.tourmanagementapi.featurs.enums.TourScheduleStatus;
import com.istad.tourmanagementapi.featurs.tour_guide.entity.TourGuide;
import com.istad.tourmanagementapi.featurs.tour_schedule.entity.TourSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TourScheduleRepository extends JpaRepository<TourSchedule, Long> {

    Optional<TourSchedule> findByIdAndIsDeletedFalse(Long id);

    Page<TourSchedule> findAllByIsDeletedFalse(Pageable pageable);

    Page<TourSchedule> findAllByStatusAndIsDeletedFalse(
            TourScheduleStatus status,
            Pageable pageable
    );

// CHECK IF THERE IS A SCHEDULE CONFLICT
    @Query("""
    SELECT COUNT(schedule) > 0
    FROM TourSchedule schedule
    JOIN schedule.guides guide
    WHERE guide.id = :guideId
      AND schedule.isDeleted = false
      AND schedule.id <> :scheduleId
      AND schedule.startDate <= :endDate
      AND schedule.endDate >= :startDate
""")
    boolean existsGuideScheduleConflict(
            @Param("guideId") Long guideId,
            @Param("scheduleId") Long scheduleId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    Page<TourSchedule> findAllByTourIdAndIsDeletedFalse(
            Long tourId,
            Pageable pageable
    );
  // Find all guides assigned to a schedule
  @Query("""
        SELECT g
        FROM TourSchedule s
        JOIN s.guides g
        WHERE s.id = :scheduleId
          AND s.isDeleted = false
    """)
  List<TourGuide> findGuidesByScheduleId(
          @Param("scheduleId") Long scheduleId
  );

    @Query("""
    SELECT COUNT(s)
    FROM TourSchedule s
    WHERE s.isDeleted = false
      AND s.startDate >= :today
      AND s.startDate <= :fiveDaysLater
""")
    Long countUpcomingTours(
            @Param("today") LocalDate today,
            @Param("fiveDaysLater") LocalDate fiveDaysLater
    );

    @Query("""
    SELECT COUNT(s)
    FROM TourSchedule s
    WHERE s.isDeleted = false
      AND s.status = :status
""")
    Long countToursByStatus(
            @Param("status") TourScheduleStatus status
    );

}