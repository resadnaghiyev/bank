package com.rashad.bank.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private String baseLocalUrl = "http://localhost:8081/api/v1";
    private String baseDevUrl = "http://localhost:8081/api/v1";

    @Bean
    public OpenAPI springStadiumOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UNIBANK API")
                        .description("API")
                        .version("v0.0.1")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .servers(List.of(
                        new Server().description("Local").url(baseLocalUrl),
                        new Server().description("Service").url(baseDevUrl)))
                .components(new Components()
                        .addSecuritySchemes("BearerJwt", new SecurityScheme()
                                .name("BearerJwt")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Bearer token for the authorization")));
    }
}
