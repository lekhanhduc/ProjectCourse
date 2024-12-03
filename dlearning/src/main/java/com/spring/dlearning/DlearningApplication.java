package com.spring.dlearning;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EnableScheduling
@EnableCaching
@OpenAPIDefinition(
        info = @Info(
                title = "DVN Learning Platform API",
                version = "1.0",
                description = "Comprehensive API documentation for DVN Learning, an advanced online learning platform designed to deliver engaging and interactive educational experiences. " +
                        "The platform offers a wide range of courses, utilizing modern technologies to ensure an intuitive and dynamic learning journey for students and educators alike. " +
                        "DVN Learning provides a seamless learning environment, featuring personalized content, real-time interaction, and easy access to various educational resources, " +
                        "empowering users to achieve their educational goals effectively.",
                contact = @Contact(
                        name = "DVN Learning Support",
                        email = "support@dvnlearning.com",
                        url = "https://www.dvnlearning.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                ),
                termsOfService = "https://www.dvnlearning.com/terms-of-service"
        ),
        servers = {
                @Server(url = "https://api.dvnlearning.com", description = "Production Server"),
                @Server(url = "https://staging.api.dvnlearning.com", description = "Staging Server")
        }
)

public class DlearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(DlearningApplication.class, args);
    }

}
