package com.istad.tourmanagementapi.featurs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";
    private static final String MANAGER = "MANAGER";
    private static final String GUIDE = "GUIDE";
    private static final String CUSTOMER = "CUSTOMER";
    private static final String[] ALL = {ADMIN, MANAGER, GUIDE, CUSTOMER};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Stateless REST API (JWT issued by Keycloak)
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrf -> csrf.disable());


        http.authorizeHttpRequests(auth -> auth

                // ═════════════ PUBLIC ═════════════
                .requestMatchers(POST, "/api/v1/auth/register").permitAll()

                // ═════════════ DESTINATIONS  (/api/v1/destinations) ═════════════
                // ADMIN/MANAGER can create destination
                .requestMatchers(POST,   "/api/v1/destinations").hasAnyAuthority(ADMIN, MANAGER)
                // ALL ROLE can get destinations
                .requestMatchers(GET,    "/api/v1/destinations").hasAnyAuthority(ALL)
                // ALL ROLE can get destination images
                .requestMatchers(GET,    "/api/v1/destinations/*/media/*").hasAnyAuthority(ALL)
                // ALL ROLE can get search destinations
                .requestMatchers(GET,    "/api/v1/destinations/search").hasAnyAuthority(ALL)
                // ALL ROLE can get destination by id
                .requestMatchers(GET,    "/api/v1/destinations/*").hasAnyAuthority(ALL)
                .requestMatchers(PATCH,  "/api/v1/destinations/*").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(DELETE, "/api/v1/destinations/*").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(PUT,    "/api/v1/destinations/*/images/*").hasAnyAuthority(ADMIN, MANAGER)

                // ═════════════ TOURS  (/api/v1/tours) ═════════════
                // ADMIN/MANAGER can create tour
                .requestMatchers(POST,   "/api/v1/tours").hasAnyAuthority(ADMIN, MANAGER)
                // ALL ROLE can get tours
                .requestMatchers(GET,    "/api/v1/tours").hasAnyAuthority(ALL)
                // ALL ROLE can get tour images
                .requestMatchers(GET,    "/api/v1/tours/*/media/*").hasAnyAuthority(ALL)
                // ALL ROLE can get search tours
                .requestMatchers(GET,    "/api/v1/tours/search").hasAnyAuthority(ALL)
                // ALL ROLE can get Popular tours
                .requestMatchers(GET,    "/api/v1/tours/popular").hasAnyAuthority(ALL)
                // ALL ROLE can get tour by destination
                .requestMatchers(GET,    "/api/v1/tours/destination/*").hasAnyAuthority(ALL)
                // ALL ROLE can get tour by id
                .requestMatchers(GET,    "/api/v1/tours/*").hasAnyAuthority(ALL)
                .requestMatchers(PATCH,  "/api/v1/tours/*").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(DELETE, "/api/v1/tours/*").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(PUT,    "/api/v1/tours/*/images/*").hasAnyAuthority(ADMIN, MANAGER)

                // ═════════════ ACTIVITIES  (/api/v1/activity) ═════════════
                .requestMatchers(POST,   "/api/v1/activity").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(GET,    "/api/v1/activity").hasAnyAuthority(ALL)
                // ALL ROLE can get activity by tour
                .requestMatchers(GET,    "/api/v1/activity/tour/*").hasAnyAuthority(ALL)
                // ALL ROLE can get activity by id
                .requestMatchers(GET,    "/api/v1/activity/*").hasAnyAuthority(ALL)
                .requestMatchers(PUT,    "/api/v1/activity/*").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(DELETE, "/api/v1/activity/*").hasAnyAuthority(ADMIN, MANAGER)

                // ═════════════ TOUR SCHEDULES  (/api/v1/tour-schedule) ═════════════
                // ADMIN/MANAGER can create tour schedule
                .requestMatchers(POST,   "/api/v1/tour-schedule").hasAnyAuthority(ADMIN, MANAGER)
                // ALL ROLE can get tour schedules
                .requestMatchers(GET,    "/api/v1/tour-schedule").hasAnyAuthority(ALL)
                //ADMIN/MANAGER can get tour schedule by status
                .requestMatchers(GET,    "/api/v1/tour-schedule/status/*").hasAnyAuthority(ADMIN, MANAGER)
                // ALL ROLE can get tour schedule by tour
                .requestMatchers(GET,    "/api/v1/tour-schedule/tour/*").hasAnyAuthority(ALL)
                // ALL ROLE can get tour schedule by guide
                .requestMatchers(GET,    "/api/v1/tour-schedule/*/guides").hasAnyAuthority(ALL)
                // ALL ROLE can get tour schedule by id
                .requestMatchers(GET,    "/api/v1/tour-schedule/*").hasAnyAuthority(ALL)
                .requestMatchers(PATCH,  "/api/v1/tour-schedule/*").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(DELETE, "/api/v1/tour-schedule/*").hasAnyAuthority(ADMIN, MANAGER)
                // ADMIN/MANAGER can assign and unassign tour guide to tour schedule
                .requestMatchers(POST,   "/api/v1/tour-schedule/*/guides/*").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(DELETE, "/api/v1/tour-schedule/*/guides/*").hasAnyAuthority(ADMIN, MANAGER)

                // ═════════════ TOUR GUIDES  (/api/v1/tour-guide) ═════════════
                // ADMIN/MANAGER can create tour guide
                .requestMatchers(POST,   "/api/v1/tour-guide").hasAnyAuthority(ADMIN,MANAGER)
                .requestMatchers(GET,    "/api/v1/tour-guide").hasAnyAuthority(ALL)
                .requestMatchers(GET,    "/api/v1/tour-guide/*").hasAnyAuthority(ALL)
                // ADMIN/MANAGER GUIDE can get user by guid id
                .requestMatchers(GET,    "/api/v1/tour-guide/*/user").hasAnyAuthority(ADMIN, MANAGER, GUIDE)
                // ALL ROLE can get tour guide schedules
                .requestMatchers(GET,    "/api/v1/tour-guide/*/schedules").hasAnyAuthority(ALL)
                .requestMatchers(PATCH,  "/api/v1/tour-guide/*").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(DELETE, "/api/v1/tour-guide/*").hasAnyAuthority(ADMIN, MANAGER)

                // ═════════════ BOOKINGS  (/api/v1/bookings) ═════════════
                // Customer can create booking
                .requestMatchers(POST,   "/api/v1/bookings").hasAuthority(CUSTOMER)
                // ADMIN / Manager can get all bookings
                .requestMatchers(GET,    "/api/v1/bookings").hasAnyAuthority(ADMIN, MANAGER)
                // Customer  can get only own bookings by status
                .requestMatchers(GET,    "/api/v1/bookings/my").hasAuthority(CUSTOMER)
                // ADMIN / Manager can get all bookings by status
                .requestMatchers(GET,    "/api/v1/bookings/status/*").hasAnyAuthority(ADMIN, MANAGER)
                // ADMIN / MANAGER Guide can get all bookings by schedule
                .requestMatchers(GET,    "/api/v1/bookings/schedule/*").hasAnyAuthority(ADMIN, MANAGER, GUIDE)
                // ADMIN / MANAGER / CUSTOMER can get booking by id
                .requestMatchers(GET,    "/api/v1/bookings/*").hasAnyAuthority(ADMIN, MANAGER, CUSTOMER)
                // Customer can cancel their own booking
                .requestMatchers(PATCH,  "/api/v1/bookings/*/cancel").hasAnyAuthority(CUSTOMER)

                // ═════════════ PARTICIPANTS  (/api/v1/participants) ═════════════
                // Customer can create participant
                .requestMatchers(POST,   "/api/v1/participants").hasAuthority(CUSTOMER)
                .requestMatchers(GET,    "/api/v1/participants").hasAnyAuthority(ALL)
                .requestMatchers(GET,    "/api/v1/participants/*").hasAnyAuthority(ALL)
                // Customer can update participant
                .requestMatchers(PUT,    "/api/v1/participants/*").hasAuthority(CUSTOMER)
                // Customer can delete participant
                .requestMatchers(DELETE, "/api/v1/participants/*").hasAuthority(CUSTOMER)

                // ═════════════ PAYMENTS  (/api/v1/payments) ═════════════
                .requestMatchers(POST,   "/api/v1/payments").hasAuthority(CUSTOMER)
                .requestMatchers(GET,    "/api/v1/payments/booking/*/can-pay").hasAnyAuthority(CUSTOMER)

                // ═════════════ REVIEWS  (/api/v1/reviews) ═════════════
                // Customer can create review
                .requestMatchers(POST,   "/api/v1/reviews").hasAuthority(CUSTOMER)
                // Customer can get their own reviews
                .requestMatchers(GET,    "/api/v1/reviews/my").hasAuthority(CUSTOMER)
                //AlL Role can get reviews
                .requestMatchers(GET,    "/api/v1/reviews/booking/*").hasAnyAuthority(ALL)
                //ALL Role can get reviews by tour
                .requestMatchers(GET,    "/api/v1/reviews/tour/*").hasAnyAuthority(ALL)
                .requestMatchers(GET,    "/api/v1/reviews/*").hasAnyAuthority(ALL)
                .requestMatchers(PATCH,  "/api/v1/reviews/*").hasAuthority(CUSTOMER)
                // Customer deletes own review (checked in service); ADMIN / MANAGER moderate
                .requestMatchers(DELETE, "/api/v1/reviews/*").hasAnyAuthority(ADMIN, MANAGER, CUSTOMER)

                // ═════════════ REPORTS  (/api/v1/reports) ═════════════
                // Admin / Manager can get system reports
                .requestMatchers(GET,    "/api/v1/reports/summary").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(GET,    "/api/v1/reports/bookings/status").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(GET,    "/api/v1/reports/tours/top").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(GET,    "/api/v1/reports/destinations/top").hasAnyAuthority(ADMIN, MANAGER)

                // ═════════════ USERS(/api/v1/users) ═════════════
                // Only Admin can create User account
                .requestMatchers(POST,   "/api/v1/users").hasAuthority(ADMIN)
                // Admin and Manager can get All users
                .requestMatchers(GET,    "/api/v1/users").hasAnyAuthority(ADMIN, MANAGER)
                // ALL Role can get their profile
                .requestMatchers(GET,    "/api/v1/users/*").hasAnyAuthority(ALL)
                // ALL Role can update their profile
                .requestMatchers(PATCH,  "/api/v1/users/*").hasAnyAuthority(ALL)
                 // ALL Role can update their profile image
                .requestMatchers(PATCH,  "/api/v1/users/*/image").hasAnyAuthority(ALL)
                // Only Admin can change User account roles and status
                .requestMatchers(PATCH,  "/api/v1/users/*/role").hasAuthority(ADMIN)
                .requestMatchers(PATCH,  "/api/v1/users/*/status").hasAuthority(ADMIN)
                // Only Admin can delete User account
                .requestMatchers(DELETE, "/api/v1/users/*").hasAuthority(ADMIN)

                // ═════════════ DEFAULT:  anything not listed above Allow to ALL Role ═════════════
                .anyRequest().hasAnyAuthority(ALL)
        );
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        Converter<Jwt, Collection<GrantedAuthority>> converter = jwt -> {

            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            if (realmAccess == null
                    || !(realmAccess.get("roles") instanceof Collection<?> roles)) {
                return Set.<GrantedAuthority>of();
            }

            return roles.stream()
                    .map(role -> (GrantedAuthority)
                            new SimpleGrantedAuthority(String.valueOf(role)))
                    .collect(Collectors.toSet());
        };

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtAuthenticationConverter;
    }
}