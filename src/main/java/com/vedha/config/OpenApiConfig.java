package com.vedha.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot JWT Security",
                version = "1.0",
                description = "Spring Boot JWT Security API Documentation",
                summary = "Spring Boot JWT Security API Documentation",
                contact = @Contact(name = "Vedha", email = "sarath1242000@gmail.com"),
                license = @License(name = "All rights reserved"),
                termsOfService = "All rights reserved"
        ),
        servers = {
                @Server(description = "Local Server", url = "http://localhost:8080"),
                @Server(description = "Production Server", url = "http://localhost:8080")
        }
//        , security = @SecurityRequirement(name = "bearerAuth") // enable global security
)
@SecuritySchemes(
        @SecurityScheme(
                name = "bearerAuth",
                description = "JWT Token",
                scheme = "bearer",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER
        )
)
public class OpenApiConfig {
}
