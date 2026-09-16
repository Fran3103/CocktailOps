package com.cocktailops.CocktailOps.dto.shopDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ShopRequestDto(

        @Schema(
                description = "Nombre de la tienda.",
                example = "Proveedor Central"
        )
        String name,

        @Schema(
                description = "Slug utilizado para identificar la tienda.",
                example = "proveedor-central"
        )
        String slug

) {
}