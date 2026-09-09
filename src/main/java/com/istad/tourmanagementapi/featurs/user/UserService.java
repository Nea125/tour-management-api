package com.istad.tourmanagementapi.featurs.user;

import com.istad.tourmanagementapi.featurs.user.dto.UserRequest;
import com.istad.tourmanagementapi.featurs.user.dto.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse findById(Long id);

    Page<UserResponse> findAll(int page, int size);

    UserResponse update(Long id, UserRequest request);

    void delete(Long id);
}
