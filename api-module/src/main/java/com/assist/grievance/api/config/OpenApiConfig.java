package com.assist.grievance.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.servers}")
    private String serverUrls;

    @Value("${openapi.server.description}")
    private String serverDescriptions;

    @Bean
    public OpenAPI productServiceAPI() {

        List<String> urlList = Arrays.asList(serverUrls.split(","));
        List<String> descList = Arrays.asList(serverDescriptions.split(","));

        List<Server> servers = urlList.stream().map(url -> {
            int index = urlList.indexOf(url);
            String description = index < descList.size() ? descList.get(index) : "Default Server";
            return new Server().url(url).description(description);
        }).collect(Collectors.toList());

        // Security Scheme
        SecurityScheme basicAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .name("basicAuth");

        SecurityRequirement securityRequirement =
                new SecurityRequirement()
                        .addList("basicAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("Grievance Service API")
                        .description("Grievance workflow APIs")
                        .version("v0.0.1")
                        .license(new License().name("SHISHUPAL LOHANI"))
                        .contact(new Contact()
                                .email("shishupallohani26@gmail.com")))
                .servers(servers)
                .components(new Components()
                        .addSecuritySchemes("basicAuth", basicAuth));

    }
}
