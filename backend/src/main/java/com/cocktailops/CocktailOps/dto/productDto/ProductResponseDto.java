package com.cocktailops.CocktailOps.dto.productDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductResponseDto(
        @Schema(description = "id del producto", example = "1")
        Long productId,
        @Schema(description = "nombre del producto", example = "vodka")
        String name,
        @Schema(description = "id de la categoría del producto", example = "2")
        Long categoryId,
        @Schema(description = "nombre de la categoría del producto", example = "Licores")
        String categoryName,
        @Schema(description = "unidad de medida del producto", example = "ml")
        String unit,
        @Schema(description = "URL de la imagen del producto", example = "https://example.com/image.jpg")
        String imageUrl,
        @Schema(description = "texto alternativo de la imagen del producto", example = "Imagen del producto Vodka")
        String imageAlt,
        @Schema(description = "indica si el producto está activo o inactivo", example = "true")
        Boolean active,
        @Schema(description = "tamaño de la unidad del producto, dependiendo de su unidad de medida", example = "750")
        BigDecimal unitSize
) {
}
