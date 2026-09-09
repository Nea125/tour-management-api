package com.istad.tourmanagementapi.featurs.schedule;

import com.istad.tourmanagementapi.featurs.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
