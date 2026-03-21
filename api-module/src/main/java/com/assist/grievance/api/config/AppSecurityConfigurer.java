package com.assist.grievance.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AppSecurityConfigurer {

    @Bean
    public SecurityFilterChain securityConfig(HttpSecurity http)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((req) -> req

                        // Swagger Open — working with openApiConfig
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Open APIs — No Login
                        .requestMatchers(
                                "/api/v1/grievance/summary"
                        ).permitAll()

                        // Secured APIs — Login Required
                        .requestMatchers(
                                "/api/v1/grievance/paginated-search"
                        ).authenticated()

                        // all remaining secured
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}