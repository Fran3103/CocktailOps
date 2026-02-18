package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderItemsResponseDto(
        @Schema(description = "id del producto", example = "1")
        Long productId,
        @Schema(description = "nombre del producto", example = "Ron blanco")
        String productName,
        @Schema(description = "cantidad de paquetes a comprar", example = "2")
        Integer packsToBuy,
        @Schema(description = "tamaño del producto", example = "750")
        java.math.BigDecimal packSize,
        @Schema(description = "unidad de medida del producto", example = "ml")
        String measureUnit
) {
}
