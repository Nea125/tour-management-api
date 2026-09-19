package com.istad.tourmanagementapi.featurs.profile;

import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.media.MediaService;
import com.istad.tourmanagementapi.featurs.profile.dto.CreateUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.PatchUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.UserProfileResponse;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import com.istad.tourmanagementapi.featurs.profile.mapper.UserProfileMapper;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userRepository;
    private final UserProfileMapper userProfileMapper;
    private final Keycloak keycloak;
    private final MediaService mediaService;

    @Value("${keycloak.realm}")
    private String realm;

    // CREATE USER
    @Override
    public UserProfileResponse create(
            CreateUserProfileRequest request
    ) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User already exists with email: "
                            + request.email()
            );
        }

        // 1. Create Keycloak user

        UserRepresentation keycloakUser =
                new UserRepresentation();

        keycloakUser.setUsername(request.userName());
        keycloakUser.setEmail(request.email());
        keycloakUser.setFirstName(request.firstName());
        keycloakUser.setLastName(request.lastName());


        CredentialRepresentation credential = new CredentialRepresentation();

        // Prepare password credential

        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());


        // Default values
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(true);
        keycloakUser.setCredentials(List.of(credential));

        Response response =
                keycloak
                        .realm(realm)
                        .users()
                        .create(keycloakUser);

        if (response.getStatus() != 201) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Failed to create user in Keycloak"
            );
        }

        String keycloakUserId = CreatedResponseUtil.getCreatedId(response);
        //  Create UserProfile

        UserProfile userProfile = new UserProfile();

        // Keycloak user ID = Profile ID
        userProfile.setId(keycloakUserId);
        userProfile.setDeleted(false);

        userProfile.setFirstName(request.firstName());

        userProfile.setLastName(request.lastName());

        userProfile.setEmail(request.email());

        userProfile.setPhone(request.phone()
        );

        userProfile.setGender(request.gender());

        userProfile.setDateOfBirth(request.dateOfBirth());

        userProfile.setStatus(UserStatus.ACTIVE);

        // Save profile


        UserProfile savedUser = userRepository.save(userProfile);

        return userProfileMapper.toResponse(savedUser);
    }




    @Override
    public PageResponse<UserProfileResponse> findAll(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<UserProfileResponse> users =
                userRepository
                        .findAllByIsDeletedFalse(pageable)
                        .map(userProfileMapper::toResponse);

        return PageResponse.<UserProfileResponse>builder()
                .items(users.getContent())
                .size(users.getSize())
                .pageNumber(users.getNumber())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
    }



    // UPDATE USER PROFILE


    @Override
    public UserProfileResponse update(String id, PatchUserProfileRequest request
    ) {

        UserProfile userProfile =
                getById(id);
        // Update Keycloak
        UserResource userResource =
                keycloak
                        .realm(realm)
                        .users()
                        .get(id);

        UserRepresentation keycloakUser =
                userResource.toRepresentation();


        // First name
        if (request.firstName() != null) {

            keycloakUser.setFirstName(
                    request.firstName()
            );
        }


        // Last name
        if (request.lastName() != null) {

            keycloakUser.setLastName(
                    request.lastName()
            );
        }


        // Email
        if (request.email() != null
                && !request.email()
                .equals(userProfile.getEmail())) {

            if (userRepository.existsByEmail(
                    request.email()
            )) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Email already exists: "
                                + request.email()
                );
            }

            keycloakUser.setEmail(
                    request.email()
            );
        }

        userResource.update(
                keycloakUser
        );



        // Update PostgreSQL

        userProfileMapper.updateEntity(
                request,
                userProfile
        );

        UserProfile updatedUser =
                userRepository.save(
                        userProfile
                );

        return userProfileMapper.toResponse(
                updatedUser
        );
    }
    @Override
    @Transactional
    public UserProfileResponse updateProfileImage(
            String id,
            MultipartFile image
    ) {
        UserProfile userProfile = getById(id);

        Media profileImage = userProfile.getProfileImage();

        if (profileImage == null) {

            // First upload
            profileImage = mediaService.uploadMediaEntity(image);

            userProfile.setProfileImage(profileImage);

        } else {

            // Replace existing image
            mediaService.updateMedia(
                    profileImage.getId(),
                    image
            );
        }

        UserProfile updatedUserProfile =
                userRepository.save(userProfile);

        return userProfileMapper.toResponse(
                updatedUserProfile

        );
    }

    @Override
    public UserProfileResponse findById(String userId) {

        UserProfile userProfile =
                userRepository
                        .findByIdAndIsDeletedFalse(userId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "User profile not found with id: "
                                                + userId
                                )
                        );

        return userProfileMapper.toResponse(userProfile);
    }


    // DELETE USER

    @Override
    public void delete(String id) {

        UserProfile user =
                getById(id);

        user.setDeleted(true);

        userRepository.save(user);
    }


    // GET ACTIVE USER


    private UserProfile getById(String id) {

        return userRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User profile not found with id: "
                                        + id
                        )
                );
    }


}