package com.company.timetracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/api/actuator/**").permitAll()
                .requestMatchers("/api/health/**").permitAll()
                .requestMatchers("/api/swagger-ui/**").permitAll()
                .requestMatchers("/api/v3/api-docs/**").permitAll()
                
                // Employee endpoints
                .requestMatchers(HttpMethod.GET, "/api/employees/me").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/employees/me").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/employees").hasAnyRole("MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/employees").hasAnyRole("HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasAnyRole("HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
                
                // Project endpoints
                .requestMatchers(HttpMethod.GET, "/api/projects").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/projects/**").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/projects").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasRole("ADMIN")
                
                // Task endpoints
                .requestMatchers(HttpMethod.GET, "/api/tasks").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/tasks/**").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasAnyRole("MANAGER", "ADMIN")
                
                // Time entry endpoints
                .requestMatchers(HttpMethod.GET, "/api/time-entries/my").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/time-entries").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/time-entries/**").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/time-entries/**").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/time-entries").hasAnyRole("MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/time-entries/**/approve").hasAnyRole("MANAGER", "HR_MANAGER", "ADMIN")
                
                // Vacation request endpoints
                .requestMatchers(HttpMethod.GET, "/api/vacation-requests/my").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/vacation-requests").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/vacation-requests/**").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/vacation-requests/**").hasAnyRole("EMPLOYEE", "MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/vacation-requests").hasAnyRole("MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/vacation-requests/**/approve").hasAnyRole("MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/vacation-requests/**/reject").hasAnyRole("MANAGER", "HR_MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/vacation-requests/**/skip-approval").hasAnyRole("HR_MANAGER", "ADMIN")
                
                // Admin endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        authoritiesConverter.setAuthoritiesClaimName("realm_access.roles");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        converter.setPrincipalClaimName("sub");
        
        return converter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
