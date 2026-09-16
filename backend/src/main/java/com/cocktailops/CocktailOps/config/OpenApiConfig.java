package com.cocktailops.CocktailOps.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI cocktailOpsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CocktailOps API")
                        .description("""
        API REST de CocktailOps para planificación de eventos de cócteles,
        cálculo de insumos, generación de órdenes y archivos PDF.

        La API incluye operaciones administrativas para productos, categorías,
        cócteles, recetas y tiendas. Algunas de estas operaciones están
        implementadas únicamente a nivel backend y no están expuestas
        en la interfaz web actual.

        El flujo principal de la aplicación web se centra en autenticación,
        consulta del catálogo, cálculo y creación de órdenes, generación de PDF
        e historial de órdenes.
        """)
                        .version("1.0.0")
                )
                .components(new Components()
                        .addSecuritySchemes(
                                SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description(
                                                "Ingrese el token JWT obtenido al iniciar sesión."
                                        )
                        )
                );
    }
}