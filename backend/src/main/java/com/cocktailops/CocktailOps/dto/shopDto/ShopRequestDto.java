package com.cocktailops.CocktailOps.dto.shopDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ShopRequestDto (
        @Schema(description = "nombre de la tienda", example = "Tienda de cócteles")
        String name,
        @Schema(description = "slug de la tienda", example = "tienda-de-cocteles")
        String slug

) {
}
