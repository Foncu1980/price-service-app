package com.bcnc.ecommerce.priceservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración centralizada de la documentación OpenAPI para el proyecto Price Service.
 *
 * Esta clase define los metadatos globales de la API (título, versión, descripción) y
 * los servidores disponibles. Es utilizada por Springdoc OpenAPI para generar la
 * especificación OpenAPI 3.0 automáticamente.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Price Service API",
                version = "1.3.2",
                description = "API REST para calcular el precio aplicable a un producto en función de la fecha, el identificador del producto y la cadena.",
                contact = @Contact( // Información de contacto
                        name = "Francisco Javier Dávila Foncuverta",
                        url = "https://www.linkedin.com/in/fjdf/",
                        email = "javierfontcuberta@gmail.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor local para desarrollo")
        }
)

public class OpenApiConfig
{
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"));
    }
}