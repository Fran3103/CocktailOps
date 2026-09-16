package com.cocktailops.CocktailOps.dto.categoryDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CategoryRequestDto(

        @Pattern(
                regexp = ".*\\S.*",
                message = "Category name must not be blank"
        )
        @Schema(
                description = "Nombre visible de la categoría. Es obligatorio al crear una categoría.",
                example = "Destilados"
        )
        String name,

        @Positive(message = "Shop id must be greater than 0")
        @Schema(
                description = """
                        ID de la tienda asociada a la categoría.
                        Actualmente esta relación existe a nivel backend,
                        pero el módulo de tiendas no forma parte del flujo funcional del frontend.
                        """,
                example = "1"
        )
        Long shop,

        @Pattern(
                regexp = ".*\\S.*",
                message = "Category slug must not be blank"
        )
        @Schema(
                description = "Identificador legible utilizado como slug de la categoría.",
                example = "destilados"
        )
        String slug,

        @Schema(
                description = """
                        Indica si la categoría se encuentra activa.
                        Al crear una categoría, si no se especifica se utiliza true.
                        """,
                example = "true",
                defaultValue = "true"
        )
        Boolean active

) {
}