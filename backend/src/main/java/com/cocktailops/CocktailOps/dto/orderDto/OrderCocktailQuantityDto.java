package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderCocktailQuantityDto(

        @Schema(
                description = "ID del cóctel.",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El id del cocktail es requerido")
        Long cocktailId,

        @Schema(
                description = "Cantidad exacta que se desea preparar de este cóctel.",
                example = "50",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La cantidad es requerida")
        @Positive(message = "La cantidad debe ser un número positivo")
        Integer quantity

) {
}