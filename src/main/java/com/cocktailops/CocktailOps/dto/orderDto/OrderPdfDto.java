package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record OrderPdfDto(
        @Schema(description = "id del pedido", example = "1")
        Long orderId,
        @Schema(description = "modo en que se calcula el pedido,  por cocktail o por cantidad de personas", example = "drinks")
        String mode,
        @Schema(description = "fecha de creación del pedido", example = "01/11/2026")
        String createdAt,
        @Schema(description = "número de invitados", example = "100")
        Integer guests,
        @Schema(description = "número de bebidas por persona", example = "2")
        Integer drinksPerPerson,
        @Schema(description = "duración del evento en horas", example = "4")
        Integer durationHours,
        @Schema(description = "total de bebidas", example = "200")
        Integer totalDrinks,
        @Schema(description = "lista de cócteles del pedido")
        List<OrderCocktailPdfDto> cocktails,
        @Schema(description = "lista de items del pedido")
        List<OrderItemsPdfDto> items
) {
}
