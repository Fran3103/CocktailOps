package com.cocktailops.CocktailOps.dto;

import java.util.List;

public record OrderByDrinksRequestDto(
        Integer totalDrinks,
        List<OrderCocktailQuantityDto> cocktails
) {
}
