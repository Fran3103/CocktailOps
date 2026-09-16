package com.cocktailops.CocktailOps.dto.cocktailDto;

import com.cocktailops.CocktailOps.entitie.PreparationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CocktailResponseDto(

        @Schema(
                description = "ID del cóctel.",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nombre del cóctel.",
                example = "Mojito"
        )
        String name,

        @Schema(
                description = "Descripción breve del cóctel.",
                example = "Cóctel refrescante preparado con ron, lima y menta."
        )
        String description,

        @Schema(
                description = "Método de preparación del cóctel.",
                example = "SHAKEN",
                allowableValues = {"DIRECT", "SHAKEN", "STIRRED", "FROZEN"}
        )
        PreparationType preparationType,

        @Schema(
                description = "URL de la imagen del cóctel.",
                example = "https://example.com/mojito.jpg"
        )
        String imageUrl,

        @Schema(
                description = "Texto alternativo de la imagen.",
                example = "Cóctel Mojito"
        )
        String imageAlt,

        @Schema(
                description = "Ingredientes que forman parte de la receta del cóctel."
        )
        List<CocktailIngredientResponseDto> ingredients

) {
}