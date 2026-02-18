package com.cocktailops.CocktailOps.dto.cocktailDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CocktailRequestDto(
        @Schema(description = "nombre del cocktail a crear", example = "mojito")
        String name,
        @Schema(description = "descripcion del cocktail ", example = "mojito, lleva menta y limas cocktail refrescante")
        String description,
        @Schema(description = "url de la imagen del producto")
        String imageUrl,
        @Schema(description = "nombre del alt de la imagen", example = "mojito")
        String imageAlt,
        @Schema(description = "lista de ingredientes que incluyen el cocktail, con sus medidas y unidades", example = "productId: 1, amount: 1.5, unit:ml ")
        List<CocktailIngredientRequestDto> ingredients
) {
}
