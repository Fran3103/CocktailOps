package com.cocktailops.CocktailOps.dto.cocktailDto;

import com.cocktailops.CocktailOps.entitie.MeasureUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CocktailIngredientRequestDto (
        @Schema(description = "id del producto, que sera asignado como ingrediente", example = "1")
        Long productId,
        @Schema(description = "cantidad de onza asignada al cocktail", example = "1.5, equivale a 45ml")
        BigDecimal amount,
        @Schema(description = "unidad de medida, dependiendo del producto", example = "ml, gr o unid")
        MeasureUnit unit
) {
}
