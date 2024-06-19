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
            String loginPath = "/auth/login";
            String registrationPath = "/auth/registration";
            String restorePasswordConfirmPath = "/auth/restore-password";
            String emailConfirmPath = "/auth/email";
            String swaggerPath = "/swagger-ui/*";
            String swaggerDocPath = "/api-doc";
            String swaggerDocPath2 = "/api-doc/*";
            request.requestMatchers(loginPath, registrationPath, restorePasswordConfirmPath,
                    emailConfirmPath, swaggerPath, swaggerDocPath, swaggerDocPath2).permitAll();
            request.anyRequest().authenticated();
        }).build();
    }
}
