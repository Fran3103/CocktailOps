package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.CocktailIngredient;

import java.util.List;

public record CocktailResponseDto(
        Long id,
        String name,
        String description,
        String imageUrl,
        String imageAlt,
        List<CocktailIngredientResponseDto> ingredients
) {
}
