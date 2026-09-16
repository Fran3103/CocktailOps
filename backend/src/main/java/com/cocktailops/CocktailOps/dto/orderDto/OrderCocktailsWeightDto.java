package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderCocktailsWeightDto(

        @Schema(
                description = "ID del cóctel.",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long cocktailId,

        @Schema(
                description = """
                        Prioridad utilizada para distribuir las bebidas entre los cócteles.
                        1 = Baja, 2 = Normal, 3 = Media, 4 = Alta.
                        Si no se envía un valor, se utiliza 2 (Normal).
                        """,
                example = "2",
                defaultValue = "2",
                minimum = "1",
                maximum = "4"
        )
        Integer weight
) {
}