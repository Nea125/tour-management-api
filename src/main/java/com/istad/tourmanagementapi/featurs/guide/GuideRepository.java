package com.istad.tourmanagementapi.featurs.guide;

import com.istad.tourmanagementapi.featurs.guide.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepository extends JpaRepository<Guide, Long> {
}
