package com.prism.messenger.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public AuthenticationManager authenticationManager() {
    return authentication -> {
      throw new AuthenticationServiceException("Authentication is disabled");
    };
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.addAllowedOriginPattern("*");
          config.setAllowedMethods(List.of("*"));
          config.setAllowedHeaders(List.of("*"));
          config.setAllowCredentials(true);
          return config;
        })).authorizeHttpRequests(request -> {
          String swaggerPath = "/swagger-ui/*";
          String swaggerDocPath = "/api-doc";
          String swaggerDocChildren = "/api-doc/*";
          String actuatorChildren = "/actuator/*";
          String actuatorPath = "/actuator";
          request.requestMatchers(swaggerPath, swaggerDocPath, swaggerDocChildren, actuatorPath,
              actuatorChildren).permitAll();
          request.anyRequest().authenticated();
        }).build();
  }
}
