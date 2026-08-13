package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderCocktailQuantityDto(
        @Schema(description = "id del cocktail", example = "1")
        @NotNull(message = "El id del cocktail es requerido")
        Long cocktailId,
        @Schema(description = "cantidad de ese cocktail que se quiere para el evento", example = "50")
        @NotNull(message = "La cantidad es requerida")
        @Positive(message = "La cantidad debe ser un número positivo")
        Integer quantity
) {
}
