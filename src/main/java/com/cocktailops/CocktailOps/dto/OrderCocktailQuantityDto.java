package com.cocktailops.CocktailOps.dto;

public record OrderCocktailQuantityDto(
        Long cocktailId,
        Integer quantity
) {
}
