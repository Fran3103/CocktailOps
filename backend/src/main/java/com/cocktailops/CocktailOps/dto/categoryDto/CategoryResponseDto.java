package com.cocktailops.CocktailOps.dto.categoryDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponseDto(
        @Schema(description = "id de la categoria", example = "1")
        Long id,
        @Schema(description = "nombre visible de la categoria", example = "alcohol")
        String name,
        @Schema(description = "id de la tienda a la que pertenece", example = "1")
        Long shop,
        @Schema(description = "codigo de la categoria", example = "xw11")
        String slug,
        @Schema(description = "indica si esta activa o no la categoria", example = "true")
        Boolean active
) {
}
