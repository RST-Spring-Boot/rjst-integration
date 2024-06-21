package de.rjst.rjstintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

@SpringBootApplication
@EnableIntegration
@EnableIntegrationGraphController
@EnableIntegrationManagement
@EnableFeignClients
public class RjstIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RjstIntegrationApplication.class, args);
    }

}
