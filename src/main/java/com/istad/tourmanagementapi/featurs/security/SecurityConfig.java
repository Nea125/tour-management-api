package com.istad.tourmanagementapi.featurs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // 1. Stateless REST API
        httpSecurity.sessionManagement(
                session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                )
        );

        // 2. Endpoint authorization
        httpSecurity.authorizeHttpRequests(request ->
                request

                        // =========================
                        // PUBLIC AUTH ENDPOINTS
                        // =========================
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/auth/register",
                                "/api/v1/auth/login"
                        ).permitAll()

                        // If you have other auth endpoints:
                        // .requestMatchers("/api/v1/auth/**").permitAll()

                        // =========================
                        // CATEGORIES
                        // =========================
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/categories/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/categories/**"
                        ).hasAnyAuthority("ADMIN", "STAFF")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/categories/**"
                        ).hasAnyAuthority("ADMIN", "STAFF")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/categories/**"
                        ).hasAnyAuthority("ADMIN", "STAFF")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/categories/**"
                        ).hasAnyAuthority("ADMIN")

                        // =========================
                        // ORDERS
                        // =========================
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/orders/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/orders/**"
                        ).hasAnyAuthority("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/orders/**"
                        ).hasAnyAuthority("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/orders/**"
                        ).hasAnyAuthority("ADMIN")

                        // Everything else requires JWT
                        .anyRequest().authenticated()
        );

        // 3. JWT Authentication
        httpSecurity.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(Customizer.withDefaults())
        );

        // 4. Disable CSRF for stateless REST API
        httpSecurity.csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        Converter<Jwt, Collection<GrantedAuthority>> converter = jwt -> {

            Map<String, Object> realmAccess =
                    jwt.getClaim("realm_access");

            Collection<String> roles = new HashSet<>();

            if (realmAccess != null) {
                roles = (Collection<String>) realmAccess.get("roles");
            }

            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        };

        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtAuthenticationConverter;
    }
}