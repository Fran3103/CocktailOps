package com.cocktailops.CocktailOps.dto.categoryDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponseDto(

        @Schema(description = "ID de la categoría.", example = "1")
        Long id,

        @Schema(description = "Nombre visible de la categoría.", example = "Destilados")
        String name,

        @Schema(
                description = "ID de la tienda asociada a la categoría.",
                example = "1"
        )
        Long shop,

        @Schema(
                description = "Slug de la categoría.",
                example = "destilados"
        )
        String slug,

        @Schema(
                description = "Indica si la categoría está activa.",
                example = "true"
        )
        Boolean active

) {
}