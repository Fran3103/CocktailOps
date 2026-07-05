package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderItemsPdfDto(
        @Schema(description = "id del producto", example = "1")
        Long productId,
        @Schema(description = "nombre del producto", example = "Ron blanco")
        String productName,
        @Schema(description = "cantidad de botellas a comprar", example = "5")
        Integer packsToBuy,
        @Schema(description = "tamaño de la medida de la botella", example = "750")
        java.math.BigDecimal packSize,
        @Schema(description = "unidad de medida del producto", example = "ml")
        String measureUnit,
        @Schema(description = "cantidad total de medida a comprar", example = "3750")
        java.math.BigDecimal totalToBuy // packsToBuy * packSize
) {
}