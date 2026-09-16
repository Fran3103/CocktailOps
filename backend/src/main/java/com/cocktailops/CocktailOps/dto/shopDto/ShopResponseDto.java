package com.cocktailops.CocktailOps.dto.shopDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ShopResponseDto(

        @Schema(description = "ID de la tienda.", example = "1")
        Long id,

        @Schema(
                description = "Nombre de la tienda.",
                example = "Proveedor Central"
        )
        String name,

        @Schema(
                description = "Slug de la tienda.",
                example = "proveedor-central"
        )
        String slug

) {
}