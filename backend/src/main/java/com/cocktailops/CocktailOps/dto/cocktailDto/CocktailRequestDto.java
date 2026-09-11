package com.cocktailops.CocktailOps.dto.cocktailDto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.cocktailops.CocktailOps.entitie.PreparationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CocktailRequestDto(

        @NotBlank(message = "Cocktail name is required")
        @Schema(description = "nombre del cocktail a crear", example = "mojito")
        String name,

        @Schema(
                description = "descripcion del cocktail",
                example = "mojito, lleva menta y limas cocktail refrescante"
        )
        String description,

        @NotNull(message = "Preparation type is required")
        @Schema(description = "tipo de preparación del cocktail", example = "SHAKEN")
        PreparationType preparationType,

        @Schema(description = "url de la imagen del producto")
        String imageUrl,

        @Schema(description = "nombre del alt de la imagen", example = "mojito")
        String imageAlt,

        @NotEmpty(message = "Cocktail must contain at least one ingredient")
        @Valid
        @Schema(
                description = "lista de ingredientes que incluyen el cocktail, con sus medidas y unidades",
                example = "productId: 1, amount: 1.5, unit:ml"
        )
        List<CocktailIngredientRequestDto> ingredients

) {
}