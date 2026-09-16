package com.cocktailops.CocktailOps.dto.cocktailDto;

import com.cocktailops.CocktailOps.entitie.PreparationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CocktailRequestDto(

        @NotBlank(message = "Cocktail name is required")
        @Schema(
                description = "Nombre del cóctel.",
                example = "Mojito",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String name,

        @Schema(
                description = "Descripción breve del cóctel.",
                example = "Cóctel refrescante preparado con ron, lima y menta."
        )
        String description,

        @NotNull(message = "Preparation type is required")
        @Schema(
                description = "Método de preparación utilizado para el cóctel.",
                example = "SHAKEN",
                allowableValues = {"DIRECT", "SHAKEN", "STIRRED", "FROZEN"},
                requiredMode = Schema.RequiredMode.REQUIRED
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

        @NotEmpty(message = "Cocktail must contain at least one ingredient")
        @Valid
        @Schema(
                description = "Ingredientes que componen la receta del cóctel.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        List<CocktailIngredientRequestDto> ingredients

) {
}