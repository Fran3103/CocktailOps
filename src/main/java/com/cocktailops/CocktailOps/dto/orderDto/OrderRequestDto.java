package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record OrderRequestDto(
        @Schema(description = "cantidad de invitados para el evento", example = "100")
        Integer guests,
        @Schema(description = "duración del evento en horas", example = "4")
        Integer durationHours,
        @Schema(description = "lista de cócteles del pedido")
        List<OrderCocktailsWeightDto> cocktails

) {
}
