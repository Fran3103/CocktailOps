package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderCocktailsWeightDto(
        @Schema(description = "id del cocktail", example = "1")
        Long cocktailId,
        @Schema(
                description = "Prioridad del cóctel: 1=Baja, 2=Normal, 3=Media, 4=Alta",
                example = "2",
                minimum = "1",
                maximum = "4"
        )
        Integer weight
) {
}
