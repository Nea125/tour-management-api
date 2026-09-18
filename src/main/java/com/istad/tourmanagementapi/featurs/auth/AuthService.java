package com.istad.tourmanagementapi.featurs.auth;


import com.istad.tourmanagementapi.featurs.auth.dto.RegisterRequest;
import com.istad.tourmanagementapi.featurs.auth.dto.RegisterResponse;

public interface AuthService {

     // register user into keycloak

      RegisterResponse register(RegisterRequest request);
}
