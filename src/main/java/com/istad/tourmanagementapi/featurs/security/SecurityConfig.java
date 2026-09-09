//package com.istad.tourmanagementapi.featurs.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//// Centralize security configuration
//@Configuration
//@EnableMethodSecurity // Allow Security configuration on method
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)  {
//
//        // TODO
//        // 1. REST architecture - STATELESS API = STATELESS not save user data in session
//
//        httpSecurity.sessionManagement(
//                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        // 2. Endpoint policy (public/protected endpoint)
//        // permitAll = allow all endpoint
//
//        httpSecurity.authorizeHttpRequests(request->
//                request.requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll().
//                        requestMatchers(
//                        // get all categories not security check
//                        HttpMethod.GET,
//                                "/api/v1/categories/**")
//                        .permitAll().
//                        requestMatchers(HttpMethod.POST,"/api/v1/categories/**").hasAnyAuthority("ADMIN", "STAFF").
//                        requestMatchers(HttpMethod.PUT,"/api/v1/categories/**").hasAnyAuthority("ADMIN","STAFF").
//                        requestMatchers(HttpMethod.PATCH,"/api/v1/categories/**").hasAnyAuthority("ADMIN","STAFF").
//                        requestMatchers(HttpMethod.DELETE,"/api/v1/categories/**").hasAnyAuthority("ADMIN").
//                        requestMatchers(
//                                HttpMethod.GET,
//                                        "/api/v1/orders/**")
//                                        .permitAll().
//                                        requestMatchers(HttpMethod.POST,"/api/v1/orders/**").hasAnyAuthority("CUSTOMER").
//                                        requestMatchers(HttpMethod.PUT,"/api/v1/orders/**").hasAnyAuthority("CUSTOMER").
//
//                                        requestMatchers(HttpMethod.DELETE,"/api/v1/orders/**").hasAnyAuthority("ADMIN")
//
//                        .anyRequest().
//                        authenticated());
//
//        // 3. Authentication Mechanism (HTTP Basic Authentication OAuth2,JWT)
////        httpSecurity.httpBasic(Customizer.withDefaults());
//
//        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()) );
//        // Disable CSRF (Cross-site request forgery)
//        // Event we disable it still secure because API is stateless
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//
//        Converter<Jwt, Collection<GrantedAuthority>> converter = jwt -> {
//
//            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
//
//            Collection<String> roles = new HashSet<>();
//
//            if (realmAccess != null) {
//                roles = (Collection<String>) realmAccess.get("roles");
//            }
//
//            return roles.stream()
//                    .map(role   -> new SimpleGrantedAuthority(role))
//                    .collect(Collectors.toSet());
//        };
//
//        JwtAuthenticationConverter jwtAuthenticationConverter =
//                new JwtAuthenticationConverter();
//
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
//
//        return jwtAuthenticationConverter;
//    }
//}
