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
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        // Stateless REST API
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth

                // ───────────── PUBLIC ─────────────
                .requestMatchers(POST, "/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                .requestMatchers(GET, "/media/**", "/api/v1/destinations/*/media/*", "/api/v1/tours/*/media/*").permitAll()

                // ───────────── DESTINATIONS ─────────────
                // Read: everyone. Create / edit / images / delete: ADMIN, MANAGER
                .requestMatchers(GET, "/api/v1/destinations/**").hasAnyAuthority(ALL)
                .requestMatchers("/api/v1/destinations/**").hasAnyAuthority(ADMIN, MANAGER)

                // ───────────── TOURS ─────────────
                // Read: everyone. Create / edit / images / delete: ADMIN, MANAGER
                .requestMatchers(GET, "/api/v1/tours/**").hasAnyAuthority(ALL)
                .requestMatchers("/api/v1/tours/**").hasAnyAuthority(ADMIN, MANAGER)

                // ───────────── ACTIVITIES ─────────────
                // Read: everyone. Create / edit / delete: ADMIN, MANAGER
                .requestMatchers(GET, "/api/v1/activity/**").hasAnyAuthority(ALL)
                .requestMatchers("/api/v1/activity/**").hasAnyAuthority(ADMIN, MANAGER)

                // ───────────── TOUR SCHEDULES ─────────────
                // Includes assign / unassign guide (POST / DELETE .../guides/{id})
                .requestMatchers(GET, "/api/v1/tour-schedule/**").hasAnyAuthority(ALL)
                .requestMatchers("/api/v1/tour-schedule/**").hasAnyAuthority(ADMIN, MANAGER)

                // ───────────── TOUR GUIDES ─────────────
                // A guide's own user data and schedules
                .requestMatchers(GET, "/api/v1/tour-guide/*/user", "/api/v1/tour-guide/*/schedules")
                .hasAnyAuthority(ADMIN, MANAGER, GUIDE)
                // List / detail of guides
                .requestMatchers(GET, "/api/v1/tour-guide", "/api/v1/tour-guide/*").hasAnyAuthority(ALL)
                .requestMatchers(PATCH, "/api/v1/tour-guide/*").hasAnyAuthority(ADMIN, MANAGER)
                // Create / delete guides
                .requestMatchers("/api/v1/tour-guide/**").hasAnyAuthority(ADMIN, MANAGER)

                // ───────────── BOOKINGS ─────────────

                .requestMatchers(GET, "/api/v1/bookings").hasAnyAuthority(ADMIN, MANAGER).requestMatchers(GET, "/api/v1/bookings/status").hasAnyAuthority(ADMIN, MANAGER)
                .requestMatchers(GET, "/api/v1/bookings/my").hasAnyAuthority(CUSTOMER)
                .requestMatchers(GET, "/api/v1/bookings/schedule").hasAnyAuthority(ADMIN,MANAGER,GUIDE)
                .requestMatchers(POST, "/api/v1/bookings").hasAnyAuthority(CUSTOMER)
                .requestMatchers(PATCH, "/api/v1/bookings/*/cancel").hasAnyAuthority(CUSTOMER)
//                .requestMatchers("/api/v1/bookings/**")
//                .hasAnyAuthority(ADMIN, MANAGER, CUSTOMER)

                // ───────────── PARTICIPANTS ─────────────
                .requestMatchers(GET, "/api/v1/participants").hasAnyAuthority(ALL)
                .requestMatchers(GET, "/api/v1/participants/*").hasAnyAuthority(ALL)
                .requestMatchers("/api/v1/participants/**").hasAnyAuthority(CUSTOMER)

                // ───────────── PAYMENTS ─────────────
                .requestMatchers(POST, "/api/v1/payments").hasAuthority(CUSTOMER)
                .requestMatchers(GET, "/api/v1/payments/**")
                .hasAnyAuthority(ADMIN, MANAGER, CUSTOMER)

                // ───────────── REVIEWS ─────────────
                .requestMatchers(GET, "/api/v1/reviews/my").hasAuthority(CUSTOMER)
                .requestMatchers(GET, "/api/v1/reviews/**").hasAnyAuthority(ALL)
                .requestMatchers(POST, "/api/v1/reviews").hasAuthority(CUSTOMER)
                .requestMatchers(PATCH, "/api/v1/reviews/*").hasAuthority(CUSTOMER)
                // Customer deletes own review; ADMIN / MANAGER can moderate
                .requestMatchers(DELETE, "/api/v1/reviews/*")
                .hasAnyAuthority(ADMIN, MANAGER, CUSTOMER)

                // ───────────── REPORTS ─────────────
                .requestMatchers("/api/v1/reports/**").hasAnyAuthority(ADMIN, MANAGER)

                // ───────────── USERS ─────────────
                // Only ADMIN can create User account roles and status


                .requestMatchers(POST, "/api/v1/users").hasAuthority(ADMIN)
                .requestMatchers(PATCH, "/api/v1/users/*/role", "/api/v1/users/*/status").hasAuthority(ADMIN)
                .requestMatchers(DELETE, "/api/v1/users/*").hasAuthority(ADMIN)
                .requestMatchers(GET, "/api/v1/users").hasAnyAuthority(ADMIN, MANAGER)
                // Detail, edit profile, change photo: any signed-in user
                .requestMatchers("/api/v1/users/**").hasAnyAuthority(ALL)

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

        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtAuthenticationConverter;
    }
}