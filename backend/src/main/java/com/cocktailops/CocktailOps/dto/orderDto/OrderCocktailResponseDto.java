package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderCocktailResponseDto(

        @Schema(description = "ID del cóctel.", example = "1")
        Long cocktailId,

        @Schema(description = "Nombre del cóctel.", example = "Mojito")
        String cocktailName,

        @Schema(description = "Cantidad calculada de este cóctel para la orden.", example = "100")
        Integer quantity

) {
}
