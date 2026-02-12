package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.MeasureUnit;

import java.math.BigDecimal;

public record CocktailIngredientResponseDto (
        Long productId,
        String productName,
        BigDecimal amount,
        MeasureUnit unit
){
}
