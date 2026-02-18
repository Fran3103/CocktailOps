package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderCocktailQuantityDto(
        @Schema(description = "id del cocktail", example = "1")
        Long cocktailId,
        @Schema(description = "cantidad de ese cocktail que se quiere para el evento", example = "50")
        Integer quantity
) {
}
