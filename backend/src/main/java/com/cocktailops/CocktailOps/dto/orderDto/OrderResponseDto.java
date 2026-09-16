package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

public record OrderResponseDto(

        @Schema(
                description = "ID de la orden. En una previsualización sin guardar puede ser nulo.",
                example = "15"
        )
        Long id,

        @Schema(
                description = """
                        Modo utilizado para calcular la orden.
                        TIME calcula según invitados y duración.
                        DRINKS utiliza cantidades exactas de cócteles.
                        """,
                example = "TIME",
                allowableValues = {"TIME", "DRINKS"}
        )
        String mode,

        @Schema(
                description = "Fecha y hora de creación de la orden en formato ISO 8601.",
                example = "2026-09-16T15:30:00Z"
        )
        Instant createdAt,

        @Schema(
                description = "Cantidad de invitados. Se utiliza en órdenes calculadas en modo TIME.",
                example = "100"
        )
        Integer guests,

        @Schema(
                description = """
                        Factor de consumo utilizado por persona y por hora.
                        En la configuración actual el valor base es 1.
                        """,
                example = "1"
        )
        Integer drinksPerPerson,

        @Schema(
                description = "Duración del evento en horas. Se utiliza en órdenes de modo TIME.",
                example = "4"
        )
        Integer durationHours,

        @Schema(
                description = "Cantidad total de bebidas calculadas o indicadas para la orden.",
                example = "400"
        )
        Integer totalDrinks,

        @Schema(
                description = "Estado actual de la orden.",
                example = "Draft"
        )
        String status,

        @Schema(
                description = "Lista de productos e insumos calculados para realizar la compra."
        )
        List<OrderItemsResponseDto> items,

        @Schema(
                description = "Distribución final de cócteles incluida en la orden."
        )
        List<OrderCocktailResponseDto> cocktail,

        @Schema(
                description = """
                        ID del usuario propietario de la orden.
                        Puede ser nulo cuando se trata de una previsualización que no se guarda.
                        """,
                example = "8"
        )
        Long userId

) {
}