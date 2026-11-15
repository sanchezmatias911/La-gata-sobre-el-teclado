package com.malb.gimnasio;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gimnasioOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API Gimnasio")
                        .version("1.0")
                        .description("API REST para la gestión del gimnasio.")
                )
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresá el JWT obtenido mediante POST /auth/login.")
                        )
                );
    }
}
