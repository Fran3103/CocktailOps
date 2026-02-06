package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.Shop;

public record CategoryResponseDto(
        Long id,
        String name,
        Long shop,
        String slug,
        Boolean active
) {
}
