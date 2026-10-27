
package com.istad.tourmanagementapi.featurs.auth;

import com.istad.tourmanagementapi.featurs.auth.dto.RegisterRequest;
import com.istad.tourmanagementapi.featurs.auth.dto.RegisterResponse;
import com.istad.tourmanagementapi.featurs.enums.UserRole;
import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import com.istad.tourmanagementapi.featurs.profile.UserProfileRepository;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Keycloak keycloak;
    private final UserProfileRepository userProfileRepository;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {


        if (!request.password().equals(request.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password and confirm password do not match"
            );
        }

        // Create Keycloak user
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.userName());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Prepare password credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        UsersResource usersResource = getUsersResource();
        String userId;

        // Save user into Keycloak
        try (Response response = usersResource.create(user)) {

            int status = response.getStatus();

            log.info("Keycloak response status code: {}", status);

            if (status == HttpStatus.CONFLICT.value()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        request.userName() + " already exists"
                );
            }

            if (status == HttpStatus.FORBIDDEN.value()) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Permission denied when creating Keycloak user"
                );
            }

            if (status == HttpStatus.UNAUTHORIZED.value()) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Keycloak authentication failed"
                );
            }

            if (status != HttpStatus.CREATED.value()) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to create Keycloak user. Status: " + status
                );
            }

            // Get the created Keycloak user ID
            userId = CreatedResponseUtil.getCreatedId(response);
        }

        log.info(
                "User {} created successfully. Keycloak ID: {}",
                request.userName(),
                userId
        );

        // Save user profile into PostgreSQL
        saveUserProfile(userId, request);

        // Assign CUSTOMER realm role
        assignRoles(userId, usersResource);

        return new RegisterResponse(
                userId,
                request.userName(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone(),
                request.gender(),
                request.dateOfBirth()
        );
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    private void saveUserProfile(String userId, RegisterRequest request) {

        UserProfile userProfile = new UserProfile();

        userProfile.setId(userId);
        userProfile.setEmail(request.email());
        userProfile.setFirstName(request.firstName());
        userProfile.setLastName(request.lastName());
        userProfile.setPhone(request.phone());
        userProfile.setGender(request.gender());
        userProfile.setDateOfBirth(request.dateOfBirth());
        userProfile.setRole(UserRole.ADMIN);
        userProfile.setStatus(UserStatus.ACTIVE);
        userProfile.setIsDeleted(false);

        userProfileRepository.save(userProfile);

        log.info("User profile saved successfully for user ID: {}", userId);
    }

    private void assignRoles(String userId, UsersResource usersResource) {

        // Get the created Keycloak
        var keycloakUser = usersResource.get(userId);

        // Get CUSTOMER realm role
        RoleRepresentation roleCustomer = keycloak
                .realm(realm)
                .roles()
                .get(UserRole.ADMIN.name())
                .toRepresentation();

        // Assign CUSTOMER role to user
        keycloakUser.roles()
                .realmLevel()
                .add(List.of(roleCustomer));

        // Verify assigned roles
        List<String> assignedRoles = keycloakUser.roles()
                .realmLevel()
                .listAll()
                .stream()
                .map(RoleRepresentation::getName)
                .toList();

        log.info("Realm roles assigned to user {}: {}", userId, assignedRoles);

        if (!assignedRoles.contains(UserRole.ADMIN.name())) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to assign CUSTOMER role"
            );
        }
    }
}
