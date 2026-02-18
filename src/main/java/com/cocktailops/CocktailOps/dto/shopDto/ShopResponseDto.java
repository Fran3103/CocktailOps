package com.cocktailops.CocktailOps.dto.shopDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record ShopResponseDto(
        @Schema(description = "id de la tienda", example = "1")
        Long id,
        @Schema(description = "nombre de la tienda", example = "Tienda de cócteles")
        String name,
        @Schema(description = "slug de la tienda", example = "tienda-de-cocteles")
        String slug

) {
}
