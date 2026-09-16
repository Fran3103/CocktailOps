package com.cocktailops.CocktailOps.dto.productDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDto(

        @Schema(hidden = true)
        Long id,

        @Pattern(regexp = ".*\\S.*", message = "Product name cannot be blank")
        @Schema(
                description = "Nombre del producto. Es obligatorio al crear un producto.",
                example = "Vodka"
        )
        String name,

        @Schema(
                description = "Descripción breve del producto.",
                example = "Destilado utilizado como base para diferentes cócteles."
        )
        String description,

        @Positive(message = "Category id must be a positive number")
        @Schema(
                description = "ID de la categoría asociada. Es obligatorio al crear un producto.",
                example = "2"
        )
        Long category,

        @Schema(
                description = "Unidad de medida utilizada para el producto.",
                example = "ML"
        )
        String unit,

        @Positive(message = "Unit size must be greater than 0")
        @Schema(
                description = "Contenido de cada envase o unidad de compra.",
                example = "750"
        )
        BigDecimal unitSize,

        @Schema(
                description = "Indica si el producto está activo. Es obligatorio al crear un producto.",
                example = "true"
        )
        Boolean active,

        @Schema(
                description = """
                        Indica si el producto se compra directamente.
                        Si es false, representa un producto preparado internamente.
                        Al crear un producto, si no se especifica se utiliza true.
                        """,
                example = "true",
                defaultValue = "true"
        )
        Boolean purchasable,

        @Schema(
                description = "URL de la imagen del producto.",
                example = "https://example.com/vodka.jpg"
        )
        String imageUrl,

        @Schema(
                description = "Texto alternativo utilizado para describir la imagen.",
                example = "Botella de vodka"
        )
        String imageAlt

) {
}