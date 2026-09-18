package com.istad.tourmanagementapi.featurs.profile;

import com.istad.tourmanagementapi.featurs.profile.dto.PatchUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.UserProfileResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;

public interface UserProfileService {

    UserProfileResponse create(PatchUserProfileRequest request);

    UserProfileResponse findById(String id);

    PageResponse<UserProfileResponse> findAll(int page, int size);

    UserProfileResponse update(String id, PatchUserProfileRequest request);

    void delete(String id);
}