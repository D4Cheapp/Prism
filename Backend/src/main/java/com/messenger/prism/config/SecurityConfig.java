package com.messenger.prism.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProviderConfig();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedHeaders(Collections.singletonList("*"));
            return config;
        })).authorizeHttpRequests(request -> {
            request.requestMatchers("/auth/login", "/auth/registration", "/auth/registration" +
                    "/confirm/*", "/auth/user/restore-password", "/auth/user/restore-password" +
                    "/confirm" + "/*", "/auth/user/email" + "/confirm" + "/*", "/swagger-ui/*",
                    "/api-doc", "/api" + "-doc/*").permitAll();
            request.anyRequest().authenticated();
        }).build();
    }
}
