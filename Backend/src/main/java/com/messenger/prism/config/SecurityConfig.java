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

import java.util.List;

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
            config.addAllowedOriginPattern("*");
            config.setAllowedMethods(List.of("*"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        })).authorizeHttpRequests(request -> {
            String loginPath = "/auth/login";
            String registrationPath = "/auth/registration";
            String restorePasswordConfirmPath = "/auth/restore-password";
            String emailConfirmPath = "/auth/email";
            String swaggerPath = "/swagger-ui/*";
            String swaggerDocPath = "/api-doc";
            String swaggerDocChilds = "/api-doc/*";
            String actuatorChilds = "/actuator/*";
            String actuatorPath = "/actuator";
            request.requestMatchers(loginPath, registrationPath, restorePasswordConfirmPath,
                    emailConfirmPath, swaggerPath, swaggerDocPath, swaggerDocChilds, actuatorPath
                    , actuatorChilds).permitAll();
            request.anyRequest().authenticated();
        }).build();
    }
}
