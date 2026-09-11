package com.cocktailops.CocktailOps.dto.cocktailDto;

import com.cocktailops.CocktailOps.entitie.MeasureUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CocktailIngredientRequestDto (

        @NotNull(message = "Product id is required")
        @Schema(
                description = "id del producto, que sera asignado como ingrediente",
                example = "1"
        )
        Long productId,

        @NotNull(message = "Ingredient amount is required")
        @Positive(message = "Ingredient amount must be greater than 0")
        @Schema(
                description = "cantidad asignada al cocktail",
                example = "45"
        )
        BigDecimal amount,

        @NotNull(message = "Ingredient unit is required")
        @Schema(
                description = "unidad de medida, dependiendo del producto",
                example = "ML"
        )
        MeasureUnit unit

) {
}