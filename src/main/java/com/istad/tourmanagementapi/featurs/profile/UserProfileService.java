package com.istad.tourmanagementapi.featurs.profile;

import com.istad.tourmanagementapi.featurs.profile.dto.CreateUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.PatchUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.UserProfileResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {
    UserProfileResponse create(CreateUserProfileRequest request);

    UserProfileResponse findById(String id);

    PageResponse<UserProfileResponse> findAll(int page, int size);

    UserProfileResponse update(String id, PatchUserProfileRequest request);

    UserProfileResponse updateProfileImage(String id, MultipartFile image);
    void delete(String id);
}