//package com.istad.tourmanagementapi.featurs.review.mapper;
//
//import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
//import com.istad.tourmanagementapi.featurs.review.dto.ReviewRequest;
//import com.istad.tourmanagementapi.featurs.review.dto.ReviewResponse;
//import com.istad.tourmanagementapi.featurs.review.entity.Review;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//@Mapper(componentModel = "spring")
//public interface ReviewMapper {
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "tour", ignore = true)
//    @Mapping(target = "booking", ignore = true)
//    @Mapping(target = "user", ignore = true)
//    Review toEntity(ReviewRequest request);
//
//    @Mapping(target = "bookingId", source = "booking.id")
//    @Mapping(target = "tourId", source = "tour.id")
//    @Mapping(target = "userId", source = "user.id")
//    ReviewResponse toResponse(Review review);
//}

package com.istad.tourmanagementapi.featurs.review.mapper;

import com.istad.tourmanagementapi.featurs.review.dto.ReviewRequest;
import com.istad.tourmanagementapi.featurs.review.dto.ReviewResponse;
import com.istad.tourmanagementapi.featurs.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "user", ignore = true)
    Review toEntity(ReviewRequest request);

    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "tourId", source = "tour.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ReviewResponse toResponse(Review review);
}