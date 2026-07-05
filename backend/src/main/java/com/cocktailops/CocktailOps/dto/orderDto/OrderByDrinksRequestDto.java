package com.cocktailops.CocktailOps.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record OrderByDrinksRequestDto(
        @Schema(description = "indica la cantidad de cocktails que el cliente quiere para su evento.", example = "200")
        Integer totalDrinks,
        @Schema(description = "lista de cocktails que quiere para su evento, y las cantidades de cada.", example= "cocktailId:1, quantity: 50, cocktailId:2, 150")
        List<OrderCocktailQuantityDto> cocktails
) {
}
