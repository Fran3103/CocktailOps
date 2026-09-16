package com.cocktailops.CocktailOps.dto.cocktailDto;

import com.cocktailops.CocktailOps.entitie.MeasureUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CocktailIngredientResponseDto(

        @Schema(
                description = "ID del producto utilizado como ingrediente.",
                example = "1"
        )
        Long productId,

        @Schema(
                description = "Nombre del producto utilizado como ingrediente.",
                example = "Ron blanco"
        )
        String productName,

        @Schema(
                description = "Cantidad del ingrediente utilizada en una preparación.",
                example = "45"
        )
        BigDecimal amount,

        @Schema(
                description = "Unidad de medida del ingrediente.",
                example = "ML",
                allowableValues = {"OZ", "ML", "GR", "UNID"}
        )
        MeasureUnit unit

) {
}