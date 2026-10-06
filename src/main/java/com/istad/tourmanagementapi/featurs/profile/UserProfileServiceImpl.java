package com.istad.tourmanagementapi.featurs.profile;

import com.istad.tourmanagementapi.featurs.enums.UserRole;
import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.media.MediaService;
import com.istad.tourmanagementapi.featurs.profile.dto.CreateUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.PatchUserProfileRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.UpdateUserStatusRequest;
import com.istad.tourmanagementapi.featurs.profile.dto.UserProfileResponse;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import com.istad.tourmanagementapi.featurs.profile.mapper.UserProfileMapper;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
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
        if (!request.password().equals(request.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password and confirm password do not match"
            );
        }

        if (userProfileRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User already exists with email: "
                            + request.email()
            );
        }

        // 1. Create user in Keycloak

        UserRepresentation keycloakUser = new UserRepresentation();

        keycloakUser.setUsername(request.userName());

        keycloakUser.setEmail(request.email());

        keycloakUser.setFirstName(request.firstName());

        keycloakUser.setLastName(request.lastName());

        CredentialRepresentation credential = new CredentialRepresentation();

        credential.setType(CredentialRepresentation.PASSWORD);

        credential.setValue(request.password());
        credential.setTemporary(false);
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(true);
        keycloakUser.setCredentials(List.of(credential));

        Response response = keycloak
                        .realm(realm)
                        .users()
                        .create(keycloakUser);

        if (response.getStatus() != 201) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Failed to create user in Keycloak "+response.getStatus()
            );
        }

        String keycloakUserId = CreatedResponseUtil.getCreatedId(response);
        assignRealmRoles(keycloakUserId, request.role());
        // Create  Profile
        UserProfile userProfile = new UserProfile();
        userProfile.setId(keycloakUserId);
        userProfile.setDeleted(false);
        userProfile.setFirstName(request.firstName());
        userProfile.setLastName(request.lastName());
        userProfile.setEmail(request.email());
        userProfile.setRole(request.role());
        userProfile.setPhone(request.phone());

        userProfile.setGender(request.gender());

        userProfile.setDateOfBirth(request.dateOfBirth());

        userProfile.setStatus(UserStatus.ACTIVE);

        UserProfile savedUser = userProfileRepository.save(userProfile);

        return toResponse(savedUser);
    }


    // FIND ALL
    @Override
    public PageResponse<UserProfileResponse> findAll(int page, int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfile> users = userProfileRepository.findAllByIsDeletedFalse(pageable);
        List<UserProfileResponse> items =
                users.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return PageResponse
                .<UserProfileResponse>builder()
                .items(items)
                .size(users.getSize())
                .pageNumber(users.getNumber())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
    }



    // FIND BY ID

    @Override
    public UserProfileResponse findById(
            String userId
    ) {

        UserProfile userProfile =
                userProfileRepository
                        .findByIdAndIsDeletedFalse(userId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "User profile not found with id: "
                                                + userId
                                )
                        );

        return toResponse(userProfile);
    }



    // UPDATE PROFILE


    @Override
    public UserProfileResponse update(String id, PatchUserProfileRequest request
    ) {
        UserProfile userProfile = getById(id);
        // Update Keycloak User
        UserResource userResource =
                keycloak
                        .realm(realm)
                        .users()
                        .get(id);

        UserRepresentation keycloakUser = userResource.toRepresentation();

        if (request.firstName() != null) {
            keycloakUser.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            keycloakUser.setLastName(request.lastName());
        }
        if (request.email() != null
                && !request.email()
                .equals(userProfile.getEmail())) {

            if (userProfileRepository.existsByEmail(request.email()
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

        userResource.update(keycloakUser);
        userProfileMapper.updateEntity(request, userProfile);
        UserProfile updatedUser = userProfileRepository.save(userProfile);

        return toResponse(updatedUser);
    }
    @Override
    public UserProfileResponse updateUserRole(
            String id,
            UserRole newRole
    ) {
        // Make sure the user exists in your database
        UserProfile userProfile = getById(id);

        UserResource userResource = keycloak
                .realm(realm)
                .users()
                .get(id);


        Set<String> applicationRoles = Set.of(
                "ADMIN",
                "MANAGER",
                "GUIDE",
                "CUSTOMER"
        );


        List<RoleRepresentation> currentRoles =
                userResource
                        .roles()
                        .realmLevel()
                        .listAll();


        List<RoleRepresentation> rolesToRemove =
                currentRoles.stream()
                        .filter(role ->
                                applicationRoles.contains(
                                        role.getName().toUpperCase()
                                )
                        )
                        .toList();

        if (!rolesToRemove.isEmpty()) {
            userResource
                    .roles()
                    .realmLevel()
                    .remove(rolesToRemove);
        }


        RoleRepresentation newRoleRepresentation =
                keycloak
                        .realm(realm)
                        .roles()
                        .get(newRole.name())
                        .toRepresentation();


        userResource
                .roles()
                .realmLevel()
                .add(List.of(newRoleRepresentation));


        return toResponse(userProfile);
    }

    // UPDATE PROFILE IMAGE

    @Override
    @Transactional
    public UserProfileResponse updateProfileImage(String id, MultipartFile image
    ) {

        UserProfile userProfile = getById(id);

        Media profileImage = userProfile.getProfileImage();


        if (profileImage == null) {

            profileImage = mediaService.uploadMediaEntity(image);

            userProfile.setProfileImage(profileImage);

        } else {
            mediaService.updateMedia(profileImage.getId(), image);
        }
        UserProfile updatedUserProfile = userProfileRepository.save(userProfile);
        return toResponse(updatedUserProfile);
    }



    // DELETE USER

    @Override
    public void delete(String id
    ) {

        UserProfile user = getById(id);
        user.setDeleted(true);
        userProfileRepository.save(user);
    }



//     KEYCLOAK DATA

    private UserProfileResponse toResponse(
            UserProfile userProfile
    ) {

        UserProfileResponse response = userProfileMapper.toResponse(userProfile);

        String roles = getUserRoles(userProfile.getId());
        return new UserProfileResponse(
                response.id(),
                response.firstName(),
                response.lastName(),
                response.email(),
                response.phone(),
                response.profileImage(),
                response.gender(),
                response.dateOfBirth(),
                response.isDeleted(),
                response.status(),
                roles

        );
    }

    // GET REALM ROLES
    private String getUserRoles(String userId) {
        List<String> applicationRoles = List.of(
                UserRole.ADMIN.name(),
                UserRole.MANAGER.name(),
                UserRole.GUIDE.name(),
                UserRole.CUSTOMER.name()
        );

        return keycloak
                .realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .listAll()
                .stream()
                .map(RoleRepresentation::getName)
                .filter(applicationRoles::contains).findFirst()
                .orElse(null);

    }

    private void assignRealmRoles(
            String userId,
            UserRole roleName
    ) {

        RealmResource realmResource = keycloak.realm(realm);

        // CUSTOMER / GUIDE / ADMIN/ MANAGER
        RoleRepresentation role =
                realmResource
                        .roles()
                        .get(roleName.name())
                        .toRepresentation();

        realmResource
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }



    // GET ACTIVE USER
    private UserProfile getById(
            String id
    ) {

        return userProfileRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User profile not found with id: "
                                        + id
                        )
                );
    }

    private void updateUserRole(UserResource userResource, UserRole newRole
    ) {

        RoleScopeResource realmLevel = userResource.roles().realmLevel();

        // Get current realm roles
        List<RoleRepresentation> currentRoles =
                realmLevel.listAll();

        // Roles that belong to application
        List<String> applicationRoles =
                List.of(
                        UserRole.ADMIN.name(),
                        UserRole.GUIDE.name(),
                        UserRole.MANAGER.name(),
                        UserRole.CUSTOMER.name()


                );

        // Find existing application roles
        List<RoleRepresentation> oldRoles =
                currentRoles.stream()
                        .filter(role ->
                                applicationRoles.contains(
                                        role.getName()
                                )
                        )
                        .toList();

        // Remove old application role
        if (!oldRoles.isEmpty()) {
            realmLevel.remove(oldRoles);
        }

        // Get new role from Keycloak
        RoleRepresentation newRoleRepresentation =
                keycloak
                        .realm(realm)
                        .roles()
                        .get(newRole.name())
                        .toRepresentation();

        // Add new role
        realmLevel.add(
                List.of(newRoleRepresentation)
        );
    }



    @Override
    public UserProfileResponse updateUserStatus(String id, UpdateUserStatusRequest request) {

        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + id
                ));

        userProfile.setStatus(request.status());

        UserProfile savedUser = userProfileRepository.save(userProfile);

        return userProfileMapper.toResponse(savedUser);
    }

}