package com.istad.tourmanagementapi.featurs.profile;

import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import com.istad.tourmanagementapi.featurs.profile.dto.PatchUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.UserProfileResponse;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import com.istad.tourmanagementapi.featurs.profile.mapper.UserProfileMapper;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userRepository;
    private final UserProfileMapper userMapper;
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public UserProfileResponse create(
            PatchUserProfileRequest request
    ) {

        if (request.email() != null &&
                userRepository.existsByEmail(request.email())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User already exists with email: " + request.email()
            );
        }

        UserProfile user = userMapper.toEntity(request);

        user.setStatus(UserStatus.ACTIVE);

        UserProfile savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserProfileResponse findById(String id) {

        UserProfile user = getById(id);

        return userMapper.toResponse(user);
    }

    @Override
    public PageResponse<UserProfileResponse> findAll(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<UserProfileResponse> users =
                userRepository
                        .findAllByStatus(UserStatus.ACTIVE, pageable)
                        .map(userMapper::toResponse);

        return PageResponse.<UserProfileResponse>builder()
                .items(users.getContent())
                .size(users.getSize())
                .pageNumber(users.getNumber())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
    }

    @Override
    public UserProfileResponse update(
            String id,
            PatchUserProfileRequest request
    ) {

        UserProfile userProfile = getById(id);

        UserResource userResource =
                keycloak
                        .realm(realm)
                        .users()
                        .get(id);

        UserRepresentation keycloakUser =
                userResource.toRepresentation();

        if (request.firstName() != null) {
            keycloakUser.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            keycloakUser.setLastName(request.lastName());
        }

        if (request.email() != null) {
            keycloakUser.setEmail(request.email());
        }

        userResource.update(keycloakUser);

        userMapper.updateEntity(
                request,
                userProfile
        );

        UserProfile updatedUser =
                userRepository.save(userProfile);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void delete(String id) {

        UserProfile user = getById(id);

        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
    }

    private UserProfile getById(String id) {

        return userRepository
                .findByIdAndStatus(
                        id,
                        UserStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Active user not found with id: " + id
                        )
                );
    }
}