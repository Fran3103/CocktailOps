package com.cocktailops.CocktailOps.dto.categoryDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CategoryRequestDto(
        @Pattern(
                regexp = ".*\\S.*",
                message = "Category name must not be blank"
        )
        @Schema(description = "nombre visible de la categoria", example = "alcohol")
        String name,

        @Positive(message = "Shop id must be greater than 0")
        @Schema(description = "id de la tienda a la que pertenece", example = "1")
        Long shop,

        @Pattern(
                regexp = ".*\\S.*",
                message = "Category slug must not be blank"
        )
        @Schema(description = "codigo de la categoria", example = "alcohol")
        String slug,

        @Schema(description = "indica si esta activa o no la categoria", example = "true")
        Boolean active
) {
}
