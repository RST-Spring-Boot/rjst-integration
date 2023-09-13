package de.rjst.rjstintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@EnableFeignClients
public class RjstIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RjstIntegrationApplication.class, args);
    }

}
