package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderCocktailPdfDto(
        @Schema(description = "id del cocktail", example = "1")
        Long cocktailId,
        @Schema(description = "nombre del cocktail", example = "Mojito")
        String cocktailName,
        @Schema(description = "cantidad de bebidas", example = "50")
        Integer drinks
) {
}
