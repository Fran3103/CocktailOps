package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.Category;

public record ProductResponseDto(
        String name,
        Category category,
        String unit,
        String image_url,
        String image_alt
) {
}
