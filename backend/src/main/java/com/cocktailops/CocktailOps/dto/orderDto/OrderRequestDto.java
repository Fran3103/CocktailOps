package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record OrderRequestDto(

        @Schema(
                description = "Cantidad de invitados al evento.",
                example = "100",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer guests,

        @Schema(
                description = "Duración del evento expresada en horas.",
                example = "4",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer durationHours,

        @Schema(
                description = """
                        Cócteles seleccionados para el evento junto con la prioridad
                        utilizada para distribuir el total de bebidas.
                        """,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        List<OrderCocktailsWeightDto> cocktails

) {
}