package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.CocktailIngredient;

import java.util.List;

public record CocktailRequestDto(
        String name,
        String description,
        String imageUrl,
        String imageAlt,
        List<CocktailIngredientRequestDto> ingredients
) {
}
