package com.assist.grievance.api.config;

import com.assist.grievance.Util.SecurityUtils;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignAuthConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            String token = SecurityUtils.getJwtTokenValue();
            if (token != null && !token.isEmpty()) {
                template.header("Authorization", "Bearer " + token);
            }
        };
    }
}
