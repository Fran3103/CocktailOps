package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

public record OrderResponseDto(
        @Schema(description = "id del pedido", example = "1")
        Long id,
        @Schema(description = "modo en que calcula el evento, por total de cocktails o por persona", example = "drink")
        String mode,
        @Schema(description = "fecha de creación del pedido", example = "01/11/2026")
        Instant createdAt,
        @Schema(description = "número de invitados", example = "100")
        Integer guests,
        @Schema(description = "número de bebidas por persona", example = "2")
        Integer drinksPerPerson,
        @Schema(description = "duración del evento en horas", example = "4")
        Integer durationHours,
        @Schema(description = "estado del pedido", example = "pending")
        String status,
        @Schema(description = "lista de items del pedido")
        List<OrderItemsResponseDto> items,
        @Schema(description = "lista de cócteles del pedido")
        List<OrderCocktailResponseDto> cocktail,
        @Schema(description = "ID del usuario que realizó el pedido")
        Long userId

) {
}
