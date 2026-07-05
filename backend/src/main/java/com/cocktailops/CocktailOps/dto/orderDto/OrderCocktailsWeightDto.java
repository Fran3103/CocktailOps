package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderCocktailsWeightDto(
        @Schema(description = "id del cocktail", example = "1")
        Long cocktailId,
        @Schema(description = "el peso da la indicacion de cuanta importacia tiene ese cocktail en la lista, mas peso, mas cocktail ", example = "2")
        Integer weight
) {
}
