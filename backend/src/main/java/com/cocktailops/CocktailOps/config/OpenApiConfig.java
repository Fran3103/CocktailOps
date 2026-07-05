package com.cocktailops.CocktailOps.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cocktailOpsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CocktailOps API")
                        .description("Cocktail supply planner: productos, cocktails, orders y PDF.")
                        .version("1.0.0"));
    }
}
