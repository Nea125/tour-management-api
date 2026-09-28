package com.istad.tourmanagementapi.featurs.review;

import com.istad.tourmanagementapi.featurs.review.dto.ReviewRequest;
import com.istad.tourmanagementapi.featurs.review.dto.ReviewResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;

public interface ReviewService {

    ReviewResponse create(ReviewRequest request);

    ReviewResponse findById(Long id);

    ReviewResponse findByBookingId(Long bookingId);

    PageResponse<ReviewResponse> findByTourId(
            Long tourId,
            int page,
            int size
    );

    void delete(Long id);
}