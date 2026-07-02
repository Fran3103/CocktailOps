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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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

    private static final String[] ORDER_ENDPOINTS = {
            "/orders",
            "/orders/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers(AUTH_ENDPOINTS).permitAll()

                        .requestMatchers(HttpMethod.GET, PUBLIC_CATALOG_ENDPOINTS).permitAll()

                        .requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, ADMIN_CATALOG_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, ADMIN_CATALOG_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, ADMIN_CATALOG_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, ADMIN_CATALOG_ENDPOINTS).hasRole("ADMIN")

                        .requestMatchers(ORDER_ENDPOINTS).authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}