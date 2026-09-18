package com.istad.tourmanagementapi.featurs.auth;


import com.istad.tourmanagementapi.featurs.auth.dto.RegisterRequest;
import com.istad.tourmanagementapi.featurs.auth.dto.RegisterResponse;
import com.istad.tourmanagementapi.featurs.enums.UserStatus;
import com.istad.tourmanagementapi.featurs.profile.UserProfileRepository;
import com.istad.tourmanagementapi.featurs.profile.entity.UserProfile;
import com.istad.tourmanagementapi.featurs.security.KeycloakRoleEnum;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    // Inject Keycloak instance
    private final Keycloak keycloak;
    //Inject userProfile repository
    private final UserProfileRepository userProfileRepository;

    @Value("${keycloak.realm}")
    private String realm;
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        // Create Keycloak user
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerRequest.userName());
        user.setEmail(registerRequest.email());
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());

        Map<String, List<String>> attributes = new HashMap<>();
        user.setAttributes(attributes);

        // Validate password
        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password and confirm password does not match"
            );
        }

        // Prepare password credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerRequest.password());
        user.setCredentials(List.of(credential));

        // Default values
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Save user into Keycloak
        UsersResource usersResource = keycloak.realm(realm).users();

        try (Response response = usersResource.create(user)) {

            log.info("Response status code: {}", response.getStatus());

            if (response.getStatus() == HttpStatus.FORBIDDEN.value()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        user.getUsername() + " already exists"
                );
            }

            if (response.getStatus() == HttpStatus.CREATED.value()) {

                log.info(
                        "User {} created successfully",
                        user.getUsername()
                );

                // Get the created Keycloak user
                UserRepresentation createdUser =
                        getUser(user.getUsername(), usersResource);

                // Save into user_profile table
                saveUserProfile(
                        user.getUsername(),
                        registerRequest
                );

                // Return response
                return new RegisterResponse(
                        createdUser.getId(),
                        createdUser.getUsername(),
                        createdUser.getFirstName(),
                        createdUser.getLastName(),
                        createdUser.getEmail(),
                        registerRequest.phone(),
//                        registerRequest.profileImage(),
                        registerRequest.gender(),
                        registerRequest.dateOfBirth()
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create user"
            );
        }
    }


    private void saveUserProfile(
            String username,
            RegisterRequest request
    ) {
        UserRepresentation createdUser =
                getUser(username, getUsersResource());

        UserProfile userProfile = new UserProfile();

        userProfile.setId(createdUser.getId());
        userProfile.setEmail(request.email());
        userProfile.setFirstName(request.firstName());
        userProfile.setLastName(request.lastName());
        userProfile.setPhone(request.phone());
        userProfile.setGender(request.gender());
        userProfile.setDateOfBirth(request.dateOfBirth());
//        userProfile.setProfileImage(request.profileImage());

        // If status is required
        userProfile.setStatus(UserStatus.ACTIVE);

        userProfileRepository.save(userProfile);
    }
    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    private  UserRepresentation getUser(String username, UsersResource usersResource) {
        return usersResource.search(username).getFirst();
    }

    private void assignGroups(String username, UsersResource usersResource) {
        UserRepresentation createdUser = usersResource
                .search(username)
                .getFirst();
        UserResource keycloakUser = usersResource.get(createdUser.getId());
        GroupsResource groupsResource = keycloak.realm(realm)
                .groups();
        GroupRepresentation groupEcommerce = groupsResource.groups("Ecommerce", 0, 1)
                .getFirst();
        log.info("Group Id: {}", groupEcommerce.getId());
        keycloakUser.joinGroup(groupEcommerce.getId());
    }

    private void assignRoles(String username, UsersResource usersResource) {
        // Start assigning role (USER, CUSTOMER)
        // Load created user by username from Keycloak
        UserRepresentation createdUser = usersResource
                .search(username)
                .getFirst();

        UserResource keycloakUser = usersResource.get(createdUser.getId());

        // Create RoleRepresentation
        RolesResource rolesResource = keycloak.realm(realm).roles();
        RoleRepresentation roleUser = rolesResource
                .get(KeycloakRoleEnum.USER.toString())
                .toRepresentation();
        RoleRepresentation roleCustomer = rolesResource
                .get(KeycloakRoleEnum.CUSTOMER.toString())
                .toRepresentation();

        List<RoleRepresentation> roles = List.of(roleUser, roleCustomer);
        keycloakUser.roles()
                .realmLevel()
                .add(roles);
    }
}
