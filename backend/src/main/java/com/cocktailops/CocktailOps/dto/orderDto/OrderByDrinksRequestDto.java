package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderByDrinksRequestDto(
        @Schema(description = "indica la cantidad de cocktails que el cliente quiere para su evento.", example = "200")
        @NotNull(message = "La cantidad total de cocktails es requerida")
        @Positive(message = "La cantidad total de cocktails debe ser un número positivo")
        Integer totalDrinks,
        @Schema(description = "lista de cocktails que quiere para su evento, y las cantidades de cada.", example= "cocktailId:1, quantity: 50, cocktailId:2, 150")
        @NotEmpty(message = "La lista de cocktails no puede estar vacía")
        @Valid
        List<OrderCocktailQuantityDto> cocktails

) {
}
