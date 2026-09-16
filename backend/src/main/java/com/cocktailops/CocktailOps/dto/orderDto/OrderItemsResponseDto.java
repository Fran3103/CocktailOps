package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderItemsResponseDto(

        @Schema(description = "ID del producto.", example = "1")
        Long productId,

        @Schema(description = "Nombre del producto.", example = "Ron blanco")
        String productName,

        @Schema(description = "Cantidad de envases o paquetes que se deben comprar.", example = "2")
        Integer packsToBuy,

        @Schema(description = "Contenido de cada envase o paquete.", example = "750")
        java.math.BigDecimal packSize,

        @Schema(description = "Unidad de medida del producto.", example = "ML")
        String measureUnit

) {
}
