package com.cocktailops.CocktailOps.dto.productDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductResponseDto(

        @Schema(description = "ID del producto.", example = "1")
        Long productId,

        @Schema(description = "Nombre del producto.", example = "Vodka")
        String name,

        @Schema(
                description = "Descripción breve del producto.",
                example = "Destilado utilizado como base para diferentes cócteles."
        )
        String description,

        @Schema(description = "ID de la categoría asociada.", example = "2")
        Long categoryId,

        @Schema(description = "Nombre de la categoría asociada.", example = "Destilados")
        String categoryName,

        @Schema(description = "Unidad de medida del producto.", example = "ML")
        String unit,

        @Schema(
                description = """
                        Indica si el producto debe comprarse directamente
                        o si corresponde a una preparación interna.
                        """,
                example = "true"
        )
        Boolean purchasable,

        @Schema(
                description = "URL de la imagen del producto.",
                example = "https://example.com/vodka.jpg"
        )
        String imageUrl,

        @Schema(
                description = "Texto alternativo de la imagen.",
                example = "Botella de vodka"
        )
        String imageAlt,

        @Schema(
                description = "Indica si el producto está activo.",
                example = "true"
        )
        Boolean active,

        @Schema(
                description = "Contenido de cada envase o unidad de compra.",
                example = "750"
        )
        BigDecimal unitSize

) {
}