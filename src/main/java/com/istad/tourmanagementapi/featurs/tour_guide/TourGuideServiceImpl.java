package com.istad.tourmanagementapi.featurs.tour_guide;

import com.istad.tourmanagementapi.featurs.enums.TourGuideStatus;
import com.istad.tourmanagementapi.featurs.profile.UserProfileRepository;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.PatchTourGuideRequest;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideRequest;
import com.istad.tourmanagementapi.featurs.tour_guide.dto.TourGuideResponse;
import com.istad.tourmanagementapi.featurs.tour_guide.entity.TourGuide;
import com.istad.tourmanagementapi.featurs.tour_guide.mapper.TourGuideMapper;
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
@Transactional
public class TourGuideServiceImpl implements TourGuideService {

    private final TourGuideRepository tourGuideRepository;
    private final UserProfileRepository userProfileRepository;
    private final TourGuideMapper tourGuideMapper;

    // CREATE
    @Override
    public TourGuideResponse create(TourGuideRequest request) {

        if (tourGuideRepository
                .existsByLicenseNumber(request.licenseNumber())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "License number already exists"
            );
        }

        UserProfile user =
                userProfileRepository
                        .findById(request.userId())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "User profile not found"
                                )
                        );

        if (tourGuideRepository
                .existsByUser_Id(request.userId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This user is already a tour guide"
            );
        }

        TourGuide tourGuide =
                tourGuideMapper.toEntity(request);

        tourGuide.setUser(user);
        tourGuide.setStatus(TourGuideStatus.ACTIVE);

        TourGuide saved =
                tourGuideRepository.save(tourGuide);

        return tourGuideMapper.toResponse(saved);
    }

    // FIND BY ID
    @Override
    @Transactional
    public TourGuideResponse findById(Long id) {

        TourGuide tourGuide =
                tourGuideRepository
                        .findByIdAndStatus(
                                id,
                                TourGuideStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Active tour guide not found"
                                )
                        );

        return tourGuideMapper.toResponse(tourGuide);
    }

    // FIND ALL
    @Override
    @Transactional
    public PageResponse<TourGuideResponse> findAll(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<TourGuideResponse> guides =
                tourGuideRepository
                        .findAllByStatus(
                                TourGuideStatus.ACTIVE,
                                pageable
                        )
                        .map(tourGuideMapper::toResponse);

        return PageResponse.<TourGuideResponse>builder()
                .items(guides.getContent())
                .size(guides.getSize())
                .pageNumber(guides.getNumber())
                .totalElements(guides.getTotalElements())
                .totalPages(guides.getTotalPages())
                .build();
    }

    // PATCH
    @Override
    public TourGuideResponse update(
            Long id,
            PatchTourGuideRequest request
    ) {

        TourGuide tourGuide =
                tourGuideRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Tour guide not found"
                                )
                        );

        // Check duplicate license number
        if (request.licenseNumber() != null
                && tourGuideRepository
                .existsByLicenseNumberAndIdNot(
                        request.licenseNumber(),
                        id
                )) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "License number already exists"
            );
        }

        // Update user relationship only when provided
        if (request.userId() != null) {

            UserProfile user =
                    userProfileRepository
                            .findById(request.userId())
                            .orElseThrow(() ->
                                    new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                                            "User profile not found"
                                    )
                            );

            // Check whether this user already belongs
            // to another tour guide
            if (!tourGuide.getUser()
                    .getId()
                    .equals(request.userId())
                    && tourGuideRepository
                    .existsByUser_Id(request.userId())) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "This user is already a tour guide"
                );
            }

            tourGuide.setUser(user);
        }

        // Update only non-null fields
        tourGuideMapper.updateEntity(
                request,
                tourGuide
        );

        TourGuide updated =
                tourGuideRepository.save(tourGuide);

        return tourGuideMapper.toResponse(updated);
    }

    // DELETE / DEACTIVATE
    @Override
    public void delete(Long id) {

        TourGuide tourGuide =
                tourGuideRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Tour guide not found"
                                )
                        );

        tourGuide.setStatus(TourGuideStatus.INACTIVE);

        tourGuideRepository.save(tourGuide);
    }
}