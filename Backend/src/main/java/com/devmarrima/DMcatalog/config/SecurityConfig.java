package com.devmarrima.DMcatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll()
            .anyRequest().permitAll() // libera tudo
        )
        .csrf(csrf -> csrf.disable()) // necessário p/ H2 e Postman (POST/PUT/DELETE)
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin())
        );

    return http.build();
}
}

