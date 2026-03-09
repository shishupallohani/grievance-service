package com.assist.grievance.api.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;

// HERE INTENSIONALLY IGNORED @Configuration TO AVOID BEING PICKED UP AS A GLOBAL CONFIGURATION
// ONLY FOR SPECIFIC FEIGN CLIENTS(DmsFiegnClientService IN THIS CASE)
public class FeignMultipartConfig {

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder();
    }
}
