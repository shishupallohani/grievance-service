package com.assist.grievance.mainapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.assist.grievance"
})
@EnableFeignClients(basePackages = "com.assist")
@EnableJpaRepositories(basePackages = "com.assist.grievance.data.repository")
@EntityScan(basePackages = "com.assist.grievance.data.entity")
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}

