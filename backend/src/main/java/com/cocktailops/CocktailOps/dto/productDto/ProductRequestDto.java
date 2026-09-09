package com.cocktailops.CocktailOps.dto.productDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductRequestDto(
        @Schema(description = "id del producto", example = "1")
        Long id,
        @Schema(description = "nombre del producto", example = "vodka")
        String name,
        @Schema(
                description = "descripción breve del producto",
                example = "Bitter aromático especiado utilizado en coctelería clásica"
        )
        String description,
        @Schema(description = "id de la categoría del producto", example = "2")
        Long category,
        @Schema(description = "unidad de medida del producto", example = "ml")
        String unit,
        @Schema(description = "tamaño de la unidad del producto", example = "750")
        BigDecimal unitSize,
        @Schema(description = "indica si el producto está activo", example = "true")
        Boolean active,
        @Schema(
                description = "indica si el producto se compra directamente o se prepara internamente",
                example = "true"
        )
        Boolean purchasable,
        @Schema(description = "URL de la imagen del producto", example = "https://example.com/image.jpg")
        String imageUrl,
        @Schema(description = "texto alternativo de la imagen del producto", example = "Imagen del producto Mojito")
        String imageAlt
) {
}
