package com.cocktailops.CocktailOps.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Value("${app.cors.allowed-origins:http://localhost:5173,http://127.0.0.1:5173}")
    private String allowedOrigins;

    private static final String[] SWAGGER_ENDPOINTS = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private static final String[] AUTH_ENDPOINTS = {
            "/auth/**"
    };

    private static final String[] PUBLIC_CATALOG_ENDPOINTS = {
            "/products",
            "/products/**",
            "/cocktails",
            "/cocktails/**",
            "/categories",
            "/categories/**"
    };

    private static final String[] ADMIN_ENDPOINTS = {
            "/user",
            "/user/**",
            "/shop",
            "/shop/**"
    };

    private static final String[] ADMIN_CATALOG_ENDPOINTS = {
            "/products",
            "/products/**",
            "/cocktails",
            "/cocktails/**",
            "/categories",
            "/categories/**"
    };

    private static final String[] USER_ORDER_ENDPOINTS = {
            "/orders/my-orders",
            "/orders/my-orders/**"
    };

    private static final String[] ORDER_DETAILS_ENDPOINTS = {
            "/orders",
            "/orders/by-drinks"
    };

    private static final String[] PUBLIC_ORDER_CREATE_ENDPOINTS = {
            "/orders/preview",
            "/orders/by-drinks/preview",
            "/orders/preview/pdf",
            "/orders/by-drinks/preview/pdf"
    };

    private static final String[] USER_ORDER_PDF_ENDPOINTS = {
            "/orders/*/pdf"
    };

    private static final String[] HEALTH_ENDPOINTS = {
            "/healthz"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HEALTH_ENDPOINTS).permitAll()
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers(AUTH_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_CATALOG_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_ORDER_CREATE_ENDPOINTS).permitAll()

                        .requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, ADMIN_CATALOG_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, ADMIN_CATALOG_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, ADMIN_CATALOG_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, ADMIN_CATALOG_ENDPOINTS).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, USER_ORDER_ENDPOINTS).authenticated()
                        .requestMatchers(HttpMethod.GET, USER_ORDER_PDF_ENDPOINTS).authenticated()
                        .requestMatchers(HttpMethod.GET, ORDER_DETAILS_ENDPOINTS).authenticated()
                        .requestMatchers(HttpMethod.GET, "/orders/*").authenticated()
                        .requestMatchers(HttpMethod.POST, ORDER_DETAILS_ENDPOINTS).authenticated()

                        .requestMatchers(HttpMethod.PUT, "/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/orders/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                Arrays.stream(allowedOrigins.split(","))
                        .map(String::trim)
                        .filter(origin -> !origin.isBlank())
                        .toList()
        );

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        configuration.setExposedHeaders(List.of(
                "Content-Disposition"
        ));

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}