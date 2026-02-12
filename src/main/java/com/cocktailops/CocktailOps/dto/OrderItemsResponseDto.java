package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.Cocktail;

import java.math.BigDecimal;

public record OrderItemsResponseDto(
        Long productId,
        String productName,
        Integer packsToBuy,
        java.math.BigDecimal packSize,
        String measureUnit
) {
}
