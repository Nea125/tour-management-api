package com.istad.tourmanagementapi.featurs.review;

import com.istad.tourmanagementapi.featurs.booking.BookingRepository;
import com.istad.tourmanagementapi.featurs.booking.entity.Booking;
import com.istad.tourmanagementapi.featurs.enums.BookingStatus;
import com.istad.tourmanagementapi.featurs.profile.UserProfileRepository;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import com.istad.tourmanagementapi.featurs.review.dto.ReviewRequest;
import com.istad.tourmanagementapi.featurs.review.dto.ReviewResponse;
import com.istad.tourmanagementapi.featurs.review.dto.ReviewUpdateRequest;
import com.istad.tourmanagementapi.featurs.review.entity.Review;
import com.istad.tourmanagementapi.featurs.review.mapper.ReviewMapper;
import com.istad.tourmanagementapi.featurs.security.AuthUtils;
import com.istad.tourmanagementapi.featurs.tour.TourRepository;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserProfileRepository userProfileRepository;
    private final ReviewMapper reviewMapper;
    private final TourRepository tourRepository;

    @Override
    @Transactional
    public ReviewResponse create(ReviewRequest request) {

        String userId = AuthUtils.extractUserId();

        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Booking not found"
                        )
                );

        // Check booking owner
        if (!booking.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot review this booking"
            );
        }

        // Only paid bookings can be reviewed
        if (booking.getStatus() != BookingStatus.PAID) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only Paid bookings can be reviewed"
            );
        }

        // One active review per booking
        if (reviewRepository.existsByBooking_IdAndIsDeletedFalse(
                booking.getId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This booking has already been reviewed"
            );
        }

        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User profile not found"
                        )
                );

        Review review = reviewMapper.toEntity(request);

        review.setBooking(booking);
        review.setUser(user);

        // Booking -> Schedule -> Tour
        review.setTour(
                booking.getSchedule().getTour()
        );

        // New review is active
        review.setDeleted(false);

        Review saved = reviewRepository.save(review);

        return reviewMapper.toResponse(saved);
    }

    @Override
    public ReviewResponse findById(Long id) {

        Review review = reviewRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Review not found with id: " + id
                        )
                );

        return reviewMapper.toResponse(review);
    }

    @Override
    public ReviewResponse findByBookingId(Long bookingId) {

        Review review = reviewRepository
                .findByBooking_IdAndIsDeletedFalse(bookingId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Review not found for booking: " + bookingId
                        )
                );

        return reviewMapper.toResponse(review);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        String userId = AuthUtils.extractUserId();

        Review review = reviewRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Review not found with id: " + id
                        )
                );

        // Only the review owner can delete it
        if (!review.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot delete this review"
            );
        }

        // Soft delete
        review.setDeleted(true);

        reviewRepository.save(review);
    }

    @Override
    public PageResponse<ReviewResponse> findByTourId(
            Long tourId,
            int page,
            int size
    ) {

        // Check whether tour exists
        tourRepository.findByIdAndIsDeletedFalse(tourId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Tour not found with id: " + tourId
                        )
                );

        Pageable pageable = PageRequest.of(page, size);

        Page<ReviewResponse> reviews =
                reviewRepository
                        .findByTour_IdAndIsDeletedFalse(
                                tourId,
                                pageable
                        )
                        .map(reviewMapper::toResponse);

        return PageResponse.<ReviewResponse>builder()
                .items(reviews.getContent())
                .size(reviews.getSize())
                .pageNumber(reviews.getNumber())
                .totalElements(reviews.getTotalElements())
                .totalPages(reviews.getTotalPages())
                .build();
    }

    @Override
    public PageResponse<ReviewResponse> findMyReviews(
            int page,
            int size
    ) {

        String userId = AuthUtils.extractUserId();

        Pageable pageable = PageRequest.of(page, size);

        Page<ReviewResponse> reviews =
                reviewRepository
                        .findByUser_IdAndIsDeletedFalse(
                                userId,
                                pageable
                        )
                        .map(reviewMapper::toResponse);

        return PageResponse.<ReviewResponse>builder()
                .items(reviews.getContent())
                .size(reviews.getSize())
                .pageNumber(reviews.getNumber())
                .totalElements(reviews.getTotalElements())
                .totalPages(reviews.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public ReviewResponse update(
            Long id,
            ReviewUpdateRequest request
    ) {

        String userId = AuthUtils.extractUserId();

        Review review = reviewRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Review not found with id: " + id
                        )
                );

        // Only the review owner can edit
        if (!review.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot edit this review"
            );
        }

        review.setRating(request.rating());
        review.setComment(request.comment());

        Review updated = reviewRepository.save(review);

        return reviewMapper.toResponse(updated);
    }
}