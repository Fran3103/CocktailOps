package com.cocktailops.CocktailOps.dto.cocktailDto;

import com.cocktailops.CocktailOps.entitie.MeasureUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CocktailIngredientRequestDto(

        @NotNull(message = "Product id is required")
        @Schema(
                description = "ID del producto utilizado como ingrediente.",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long productId,

        @NotNull(message = "Ingredient amount is required")
        @Positive(message = "Ingredient amount must be greater than 0")
        @Schema(
                description = "Cantidad del ingrediente utilizada en una preparación del cóctel.",
                example = "45",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal amount,

        @NotNull(message = "Ingredient unit is required")
        @Schema(
                description = "Unidad de medida correspondiente a la cantidad del ingrediente.",
                example = "ML",
                allowableValues = {"OZ", "ML", "GR", "UNID"},
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        MeasureUnit unit

) {
}