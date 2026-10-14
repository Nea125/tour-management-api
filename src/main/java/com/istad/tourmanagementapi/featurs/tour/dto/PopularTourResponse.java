package com.istad.tourmanagementapi.featurs.tour.dto;

public record PopularTourResponse(
        TourResponse tour,
        Double averageRating,
        Long reviewCount
) {
}