package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.MeasureUnit;

import java.math.BigDecimal;

public record CocktailIngredientRequestDto (
        Long productId,
        BigDecimal amount,
        MeasureUnit unit
) {
}
