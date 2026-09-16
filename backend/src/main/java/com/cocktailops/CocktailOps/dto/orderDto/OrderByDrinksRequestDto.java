package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderByDrinksRequestDto(

        @Schema(
                description = "Cantidad total de cócteles que se desea preparar para el evento.",
                example = "200",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La cantidad total de cocktails es requerida")
        @Positive(message = "La cantidad total de cocktails debe ser un número positivo")
        Integer totalDrinks,

        @Schema(
                description = """
                        Lista de cócteles seleccionados y cantidad exacta
                        que se desea preparar de cada uno.
                        """,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotEmpty(message = "La lista de cocktails no puede estar vacía")
        @Valid
        List<OrderCocktailQuantityDto> cocktails

) {
}